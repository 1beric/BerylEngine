package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import engine.animation.Animation;
import engine.audio.AudioMaster;
import engine.entities.Entity;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventHandler;
import engine.events.keyboard.KeyPressedEvent;
import engine.events.window.WindowCloseEvent;
import engine.events.window.WindowFocusEvent;
import engine.events.window.WindowLostFocusEvent;
import engine.events.window.WindowResizeEvent;
import engine.models.Texture;
import engine.rendering.MasterRenderer;
import engine.rendering.RenderType;
import engine.shaders.Shader;
import engine.util.ClassReloader;
import engine.util.Palette;
import engine.util.Time;
import engine.util.input.KeyboardPicker;
import engine.util.input.MousePicker;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Application implements Updateable, Stringable, EventHandler {

	private static Application s_Instance;

	public static Application instance() {
		return s_Instance;
	}

	private boolean m_Running;
	private List<Window> m_Windows, m_WindowsToClose;
	private List<Layer> m_Layers, m_LayersReversed, m_LayersToAdd;

	private boolean m_Initialized;

	private String m_SaveFile;

	private int m_WWidth;
	private int m_WHeight;
	private String m_WName;

	public Application(String windowName, int wW, int wH, String saveFile) {
		assert (s_Instance == null);
		s_Instance = this;

		m_SaveFile = saveFile;
		m_WName = windowName;
		m_WWidth = wW;
		m_WHeight = wH;

		m_Running = false;
		m_Windows = new ArrayList<>();
		m_WindowsToClose = new ArrayList<>();
		m_Layers = new ArrayList<>();
		m_LayersReversed = new ArrayList<>();
		m_LayersToAdd = new ArrayList<>();
		m_Initialized = false;
	}

	@Override
	public void init() {
		assert !m_Running;
		m_Running = true;
		addWindow(Window.createWindow(m_WName, m_WWidth, m_WHeight));
		MousePicker.init(primaryWindow());
		KeyboardPicker.init();
		Time.init();
		AudioMaster.init();
		Palette.loadPalettes();
		Texture.loadTextures();
//		Cubemap.loadCubemaps();
		Shader.loadShaders();
		Material.loadMaterials();
		readGameFile(m_SaveFile);
		for (Layer layer : m_Layers)
			layer.init();
		m_Initialized = true;
		handleAddLayers();
	}

	@Override
	public void update() {
		assert m_Running;

		Animation.updateAll();

		handleAddLayers();
		for (Layer layer : m_Layers)
			layer.update();

	}

	@Override
	public void lateUpdate() {
		assert m_Running;

		for (Layer layer : m_Layers)
			layer.lateUpdate();

	}

	public void render() {
		if (!m_Running)
			return;

		handleAddLayers();
		MasterRenderer.render(m_Layers);
	}

	@Override
	public boolean handleAllEvents(Event event) {

		if (event.inCategory(EventCategory.Mouse) || event.inCategory(EventCategory.MouseButton))
			MousePicker.handleEvent(event);

		if (event.inCategory(EventCategory.Keyboard))
			KeyboardPicker.handleEvent(event);

		for (Layer layer : m_LayersReversed) {
			if (layer.handleEvent(event))
				break;
		}
		return false;
	}

	@Override
	public boolean handleWindowClose(WindowCloseEvent event) {
		m_WindowsToClose.add(event.window());
		System.out.println("closing " + event.window());
		return true;
	}

	@Override
	public boolean handleWindowFocus(WindowFocusEvent event) {
//		m_FocusedWindow = event.window();
		return false;
	}

	@Override
	public boolean handleWindowLostFocus(WindowLostFocusEvent event) {
//		m_FocusedWindow = null;
		return false;
	}

	@Override
	public boolean handleWindowResize(WindowResizeEvent event) {
		event.window().width(event.width());
		event.window().height(event.height());
//		FrameBuffer.resizeAll(event.window().resolution());
		return false;
	}

	@Override
	public boolean handleKeyPressed(KeyPressedEvent kpe) {
		if (kpe.key() == GLFW.GLFW_KEY_P)
			resetApplication();
		return false;
	}

	public void updateWindows() {
		for (Window w : m_Windows)
			w.update();
		for (Window w : m_WindowsToClose) {
			m_Windows.remove(w);
			w.destroy();
		}
		m_WindowsToClose.clear();
		if (!m_Windows.isEmpty())
			GLFW.glfwPollEvents();
	}

	public void resetApplication() {
		destroyLayers();
		readGameFile(m_SaveFile);
	}

	public void readGameFile(String file) {
		try {
			Scanner scanner = new Scanner(new File(file));
			while (scanner.hasNextLine()) {
				String lineText = scanner.nextLine();
				String[] line = lineText.split("\\s*;\\s*");
				line[0] = line[0].toLowerCase();
				switch (line[0]) {
				case "layer":
					String l_Name = null;
					RenderType l_Type = RenderType.NONE;
					for (int i = 1; i < line.length; i++) {
						String[] arg = line[i].split("\\s*=\\s*");
						arg[0] = arg[0].toLowerCase();
						switch (arg[0]) {
						case "name":
							l_Name = arg[1];
							break;
						case "type":
							l_Type = RenderType.valueOf(arg[1].toUpperCase());
							break;
						}
					}
					if (l_Name == null) {
						System.out.println("ERROR COMPILING SCENE -----");
						System.out.println(lineText);
						break;
					}
					pushLayer(new Layer(l_Name, primaryWindow(), l_Type));
					break;
				case "entity":
					String e_Name = null;
					Layer e_Layer = null;
					for (int i = 1; i < line.length; i++) {
						String[] arg = line[i].split("\\s*=\\s*");
						arg[0] = arg[0].toLowerCase();
						switch (arg[0]) {
						case "name":
							e_Name = arg[1];
							break;
						case "layer":
							e_Layer = layer(arg[1]);
							break;
						}
					}
					if (e_Layer == null || e_Name == null) {
						System.out.println("ERROR COMPILING SCENE -----");
						System.out.println(lineText);
						break;
					}
					e_Layer.addEntity(new Entity(e_Name));
					break;
				case "component":
					String c_Name = null;
					String c_Prefix = null;
					Entity c_Entity = null;
					for (int i = 1; i < line.length; i++) {
						String[] arg = line[i].split("\\s*=\\s*");
						arg[0] = arg[0].toLowerCase();
						switch (arg[0]) {
						case "name":
							c_Name = arg[1];
							break;
						case "prefix":
							c_Prefix = arg[1];
							break;
						case "entity":
							c_Entity = entity(arg[1]);
							break;
						}
					}
					if (c_Name == null || c_Prefix == null || c_Entity == null) {
						System.out.println("ERROR COMPILING SCENE -----");
						System.out.println(lineText);
						break;
					}
					c_Entity.addComponent(ClassReloader.createObject(c_Prefix, c_Name));
					break;
				}
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		assert m_Running;
		m_Running = false;

		Material.writeMaterials();

		destroyLayers();
		destroyWindows();
		destroyAnimations();
	}

	private void destroyWindows() {
		for (Window w : m_Windows)
			w.destroy();
		m_Windows.clear();

		for (Window w : m_WindowsToClose)
			w.destroy();
		m_WindowsToClose.clear();
	}

	private void destroyLayers() {
		for (Layer layer : m_Layers)
			layer.destroy();
		m_Layers.clear();
	}

	private void destroyAnimations() {
		Animation.destroyAll();
	}

	public Layer layer(String name) {
		for (Layer layer : m_Layers) {
			if (layer.name().equals(name))
				return layer;
		}
		for (Layer layer : m_LayersToAdd) {
			if (layer.name().equals(name))
				return layer;
		}
		return null;
	}

	public Entity entity(String name) {
		for (Layer layer : m_Layers) {
			Entity e = layer.entity(name);
			if (e != null)
				return e;
		}
		for (Layer layer : m_LayersToAdd) {
			Entity e = layer.entity(name);
			if (e != null)
				return e;
		}
		return null;
	}

	public void pushLayer(Layer l) {
		m_LayersToAdd.add(l);
	}

	public void handleAddLayers() {
		List<Layer> copy = new ArrayList<>(m_LayersToAdd);
		m_LayersToAdd.clear();
		for (Layer layer : copy) {
			m_Layers.add(layer);
			m_LayersReversed.add(0, layer);
		}
		for (Layer layer : copy) {
			if (m_Initialized)
				layer.init();
		}
	}

	public void addWindow(Window window) {
		m_Windows.add(window);
	}

	public Window primaryWindow() {
		assert (!m_Windows.isEmpty());
		return m_Windows.get(0);
	}

	public void updateRunning() {
		assert m_Running;
		m_Running = m_Windows.size() != 0;
	}

	public boolean isRunning() {
		return m_Running;
	}

	@Override
	public String toString() {
		return string(0);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Application {\n",
				StringTools.buildString(indentAmt + 1, m_Layers), StringTools.indentl(indentAmt), "}");
	}

}
