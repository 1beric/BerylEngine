package engine.shaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.models.Cubemap;
import engine.models.Texture;
import engine.shaders.uniforms.BooleanUniform;
import engine.shaders.uniforms.ColorUniform;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.MatrixUniform;
import engine.shaders.uniforms.Sampler2DUniform;
import engine.shaders.uniforms.SamplerCubeUniform;
import engine.shaders.uniforms.Uniform;
import engine.shaders.uniforms.Vec2Uniform;
import engine.shaders.uniforms.Vec3Uniform;
import engine.shaders.uniforms.Vec4Uniform;
import engine.util.Color;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Shader implements Stringable {

//	public static final Shader DEFAULT = new Shader("default", new String[] { "position" });

	public static Map<String, Shader> s_Shaders = new HashMap<>();
	private static final String ALL_SHADERS = "shaders/allShaders.dat";

	public static void loadShaders() {
		try (Scanner scanner = new Scanner(new File(ALL_SHADERS))) {
			while (scanner.hasNextLine())
				loadShader(scanner.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Shader loadShader(String name) {
		Shader shader = null;
		try (Scanner scanner = new Scanner(new File("shaders/" + name + "/default.bsh"))) {
			while (scanner.hasNextLine()) {
				String strLine = scanner.nextLine();
				String line[] = strLine.split("\\s*;\\s*");
				Uniform uni = null;
				switch (line[0].toLowerCase()) {
				case "attrs":
				case "attributes":
					shader = new Shader(name, line[1].split("\\s*,\\s*"));
					break;
				case "booleanuniform":
					uni = shader.addUniform(new BooleanUniform(line[1], line[2].equalsIgnoreCase("true")));
					break;
				case "coloruniform":
					String[] color = line[2].split("\\s*,\\s*");
					uni = shader.addUniform(new ColorUniform(line[1], new Color(Float.parseFloat(color[0]),
							Float.parseFloat(color[1]), Float.parseFloat(color[2]))));
					break;
				case "floatuniform":
					uni = shader.addUniform(new FloatUniform(line[1], Float.parseFloat(line[2])));
					break;
				case "matrixuniform":
					uni = shader.addUniform(new MatrixUniform(line[1], new Matrix4f().identity()));
					break;
				case "sampler2duniform":
					String[] samp2d = line[2].split("\\s*,\\s*");
					uni = shader.addUniform(
							new Sampler2DUniform(line[1], Texture.texture(samp2d[0]), Integer.parseInt(samp2d[1])));
					break;
				case "samplercubeuniform":
					String[] cubeVals = line[2].split("\\s*,\\s*");
					uni = shader.addUniform(
							new SamplerCubeUniform(line[1], Cubemap.defaultMap(), Integer.parseInt(cubeVals[1])));
					break;
				case "vec2uniform":
					String[] vec2 = line[2].split("\\s*,\\s*");
					uni = shader.addUniform(new Vec2Uniform(line[1],
							new Vector2f(Float.parseFloat(vec2[0]), Float.parseFloat(vec2[1]))));
					break;
				case "vec3uniform":
					String[] vec3 = line[2].split("\\s*,\\s*");
					uni = shader.addUniform(new Vec3Uniform(line[1], new Vector3f(Float.parseFloat(vec3[0]),
							Float.parseFloat(vec3[1]), Float.parseFloat(vec3[2]))));
					break;
				case "vec4uniform":
					String[] vec4 = line[2].split("\\s*,\\s*");
					uni = shader.addUniform(new Vec4Uniform(line[1], new Vector4f(Float.parseFloat(vec4[0]),
							Float.parseFloat(vec4[1]), Float.parseFloat(vec4[2]), Float.parseFloat(vec4[3]))));
					break;
				}
				if (uni != null && line.length > 3 && line[3].equals("noSave"))
					uni.setWriteOut(false);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return shader;
	}

	public static Shader shader(String name) {
		return s_Shaders.get(name).clone();
	}

	public static void destroyAll() {
		for (String name : s_Shaders.keySet())
			s_Shaders.get(name).destroy();
	}

	private String m_Name;
	private Map<String, Uniform> m_Uniforms;
	private int m_NumAttrs;

	private int m_ProgramID, m_VertexID, m_FragmentID;

	private Shader(String name) {
		m_Name = name;
		m_Uniforms = new HashMap<>();
	}

	private Shader(String name, String[] attrs) {
		m_Name = name;
		m_Uniforms = new HashMap<>();

		// create the ids
		m_VertexID = loadShader("shaders/" + name + "/vertex.glsl", GL20.GL_VERTEX_SHADER);
		m_FragmentID = loadShader("shaders/" + name + "/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		m_ProgramID = GL20.glCreateProgram();

		// attach shaders to program
		GL30.glAttachShader(m_ProgramID, m_FragmentID);
		GL30.glAttachShader(m_ProgramID, m_VertexID);

		// need to bind attributes
		m_NumAttrs = attrs.length;
		for (int i = 0; i < attrs.length; i++)
			GL20.glBindAttribLocation(m_ProgramID, i, attrs[i]);

		// link and validate
		GL20.glLinkProgram(m_ProgramID);
		GL20.glValidateProgram(m_ProgramID);

		// add it to the static list
		s_Shaders.put(m_Name, this);

	}

	public void start() {
		GL20.glUseProgram(m_ProgramID);
		for (int i = 0; i < m_NumAttrs; i++)
			GL20.glEnableVertexAttribArray(i);
	}

	public void stop() {
		GL20.glUseProgram(0);
		for (int i = 0; i < m_NumAttrs; i++)
			GL20.glDisableVertexAttribArray(i);
	}

	public void destroy() {
		stop();
		GL20.glDetachShader(m_ProgramID, m_VertexID);
		GL20.glDetachShader(m_ProgramID, m_FragmentID);
		GL20.glDeleteShader(m_VertexID);
		GL20.glDeleteShader(m_FragmentID);
		GL20.glDeleteProgram(m_ProgramID);
	}

	public void loadUniforms() {
		for (String key : m_Uniforms.keySet())
			m_Uniforms.get(key).load();
	}

	public void unloadUniforms() {
		for (String key : m_Uniforms.keySet())
			m_Uniforms.get(key).unload();
	}

	public Uniform addUniform(Uniform uni) {
		m_Uniforms.put(uni.name(), uni);
		uni.location(m_ProgramID);
		return uni;
	}

	public <T extends Uniform> T uniform(String name) {
		return (T) m_Uniforms.get(name);
	}

	public String name() {
		return m_Name;
	}

	public Shader clone() {
		Shader out = new Shader(m_Name);
		out.m_VertexID = this.m_VertexID;
		out.m_FragmentID = this.m_FragmentID;
		out.m_ProgramID = this.m_ProgramID;
		for (String uni : this.m_Uniforms.keySet())
			out.addUniform(this.m_Uniforms.get(uni).clone());
		out.m_NumAttrs = this.m_NumAttrs;
		return out;
	}

	public String toString() {
		return name() + " {\n" + string(1) + "\n}";
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(indentAmt, m_Uniforms);
	}

	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			reader.close();
		} catch (IOException ioe) {
			System.err.println("Could not read file: " + file);
			ioe.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader from " + file);
			System.exit(-1);
		}
		return shaderID;
	}

	public Map<String, Uniform> uniforms() {
		return m_Uniforms;
	}

}
