package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.animation.Animatable;
import engine.models.Cubemap;
import engine.models.Texture;
import engine.shaders.Shader;
import engine.shaders.uniforms.Uniform;
import engine.util.Color;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Material implements Stringable, Animatable {

	private static Map<String, Material> s_Materials = new HashMap<>();
	private static final String ALL_MATERIALS = "res/materials/allMaterials.dat";

	public static void loadMaterials() {
		try (Scanner scanner = new Scanner(new File(ALL_MATERIALS))) {
			while (scanner.hasNextLine())
				loadMaterial(scanner.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Material loadMaterial(String name) {
		Material material = new Material(name);
		boolean mutability = false;

		try (Scanner scanner = new Scanner(new File("res/materials/" + name + ".bmtl"))) {
			while (scanner.hasNextLine()) {
				String line[] = scanner.nextLine().split("\\s*;\\s*");
				switch (line[0].toLowerCase()) {
				case "shader":
					material.shader(Shader.shader(line[1]));
					break;
				case "booleanuniform":
					material.uniform(line[1], line[2].equalsIgnoreCase("true"));
					break;
				case "coloruniform":
					material.uniform(line[1], Color.parse(line[2]));
					break;
				case "floatuniform":
					material.uniform(line[1], Float.parseFloat(line[2]));
					break;
				case "matrixuniform":
					material.uniform(line[1], new Matrix4f().identity());
					break;
				case "sampler2duniform":
					material.uniform(line[1], Texture.texture(line[2].split("\\s*,\\s*")[0]));
					break;
				case "samplercubeuniform":
					material.uniform(line[1], Cubemap.defaultMap());
					break;
				case "vec2uniform":
					String[] vec2 = line[2].split("\\s*,\\s*");
					material.uniform(line[1], new Vector2f(Float.parseFloat(vec2[0]), Float.parseFloat(vec2[1])));
					break;
				case "vec3uniform":
					String[] vec3 = line[2].split("\\s*,\\s*");
					material.uniform(line[1], new Vector3f(Float.parseFloat(vec3[0]), Float.parseFloat(vec3[1]),
							Float.parseFloat(vec3[2])));
					break;
				case "vec4uniform":
					String[] vec4 = line[2].split("\\s*,\\s*");
					material.uniform(line[1], new Vector4f(Float.parseFloat(vec4[0]), Float.parseFloat(vec4[1]),
							Float.parseFloat(vec4[2]), Float.parseFloat(vec4[3])));
					break;
				case "mutable":
					mutability = true;
					break;
				case "immutable":
					mutability = false;
					break;
				}
			}
			material.m_Mutable = mutability;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return material;
	}

	public static void writeMaterials() {
		try (FileOutputStream fos = new FileOutputStream(new File(ALL_MATERIALS))) {
			for (String name : s_Materials.keySet()) {
				fos.write(name.getBytes());
				fos.write('\n');
			}
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (String name : s_Materials.keySet())
			s_Materials.get(name).writeOut();
	}

	public static Material material(String name) {
		if (!s_Materials.containsKey(name))
			return loadMaterial(name);
		return s_Materials.get(name);
	}

	private Shader m_Shader;
	private String m_Name;
	private boolean m_Mutable;

	private Material(String name) {
		s_Materials.put(name, this);
		this.m_Name = name;
		this.m_Shader = null;
		this.m_Mutable = true;
	}

	private Material shader(Shader shader) {
		m_Shader = shader;
		return this;
	}

	public Shader shader() {
		return m_Shader;
	}

	public String name() {
		return m_Name;
	}

	public <T extends Uniform> T uniform(String name) {
		if (!m_Mutable)
			throw new RuntimeException("IMMUTABLE: " + m_Name);
		return m_Shader.<T>uniform(name);
	}

	public String toString() {
		return string(0);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), " {\n", m_Shader.string(indentAmt + 1),
				StringTools.indentl(indentAmt), "}");
	}

	public void writeOut() {
		if (m_Shader == null) {
			System.out.println("Can't write out: " + m_Name);
			return;
		}
		try (FileOutputStream fos = new FileOutputStream(new File("res/materials/" + m_Name + ".bmtl"))) {
			fos.write(("shader;" + m_Shader.name() + "\n").getBytes());
			if (m_Mutable)
				fos.write("mutable\n".getBytes());
			else
				fos.write("immutable\n".getBytes());
			for (String uniformName : m_Shader.uniforms().keySet()) {
				Uniform uniform = m_Shader.uniform(uniformName);
				fos.write(uniform.write().getBytes());
				fos.write('\n');
			}
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
