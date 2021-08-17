package engine.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import engine.components.Component;
import engine.entities.Entity;

public class ClassReloader extends ClassLoader {

	public ClassReloader(ClassLoader parent) {
		super(parent);
	}

	public Class<?> loadClass(String filePath, String name) throws ClassNotFoundException {
		try {
			InputStream inputStream = new File(filePath).toURI().toURL().openConnection().getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = inputStream.read();
			while (data != -1) {
				buffer.write(data);
				data = inputStream.read();
			}
			inputStream.close();
			byte[] classData = buffer.toByteArray();
			return this.defineClass(name, classData, 0, classData.length);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Entity[] recompileGroups(String sceneFile) {
		Map<String, List<String>> lines = new HashMap<>();
		try {
			Scanner scanner = new Scanner(new File(sceneFile));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("#") || line.length() == 0)
					continue;
				String[] arr = line.split(",");
				if (!lines.containsKey(arr[0]))
					lines.put(arr[0], new ArrayList<>());
				lines.get(arr[0]).add(arr[1]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found: " + sceneFile);
			e.printStackTrace();
		}
		Entity[] entities = new Entity[lines.keySet().size()];
		int i = 0;
		for (String name : lines.keySet()) {
			entities[i] = new Entity(name);
			for (String path : lines.get(name)) {
				Class<?> c = compile(path, name);
				Component component = null;
				try {
					component = (Component) c.getConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				if (component != null)
					entities[i].addComponent(component);
			}
			i++;
		}
		return entities;
	}

	public static Component createObject(String path, String name) {

		Class<?> c = compile(path, name);
		Component component = null;
		try {
			component = (Component) c.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return component;
	}

	public static Class<?> compile(String path, String name) {
		// compile
		File sourceFile = new File("src/" + path + "/" + name + ".java");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compUnits = fileManager.getJavaFileObjects(sourceFile);
		try {
			compiler.getTask(null, fileManager, null,
					Arrays.asList(new String[] { "-d", new File("bin/" + path).getCanonicalPath() }), null, compUnits)
					.call();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// reload
		return reload("bin/" + path + "/" + name + ".class", path.replaceAll("/", ".") + "." + name);
	}

	private static Class<?> reload(String dotClassPath, String name) {
		ClassLoader parentClassLoader = ClassReloader.class.getClassLoader();
		ClassReloader classLoader = new ClassReloader(parentClassLoader);
		try {
			return classLoader.loadClass(dotClassPath, name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
