package engine.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import engine.util.Color;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Texture implements Stringable {

	public static Map<String, Texture> s_Textures = new HashMap<>();

	private static final String ALL_TEXTURES = "res/textures/allTextures.dat";

	public static void loadTextures() {
		buildTexture(Color.white(), "white");
		buildTexture(Color.red(), "red");
		buildTexture(Color.green(), "green");
		buildTexture(Color.blue(), "blue");
		buildTexture(Color.yellow(), "yellow");
		buildTexture(Color.cyan(), "cyan");
		buildTexture(Color.magenta(), "magenta");
		buildTexture(Color.black(), "black");

		try (Scanner scanner = new Scanner(new File(ALL_TEXTURES))) {
			while (scanner.hasNextLine())
				loadTexture(scanner.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Texture loadTexture(String name) {
		if (name.startsWith("palette"))
			return buildTexture(Color.parse(name), name);
		else
			return new Texture(name, true);
	}

	public static Texture texture(String name) {
		Texture out = s_Textures.get(name);
		if (out == null) {
			System.out.println("NO TEXTURE: " + name);
			return new Texture(name, true);
		}
		return out;
	}

	public static Texture white() {
		return texture("white");
	}

	public static Texture red() {
		return texture("red");
	}

	public static Texture green() {
		return texture("green");
	}

	public static Texture blue() {
		return texture("blue");
	}

	public static Texture yellow() {
		return texture("yellow");
	}

	public static Texture cyan() {
		return texture("cyan");
	}

	public static Texture magenta() {
		return texture("magenta");
	}

	public static Texture black() {
		return texture("black");
	}

	private static Texture buildTexture(Color color, String name) {
		// GPU bound
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		// repeat texture
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// pixelate on stretch/shrink
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		// load colors
		float[] colors = new float[] { color.r(), color.g(), color.b() };
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB4, 1, 1, 0, GL11.GL_RGB, GL11.GL_FLOAT, colors);

		Texture out = new Texture(name, textureID);
		out.m_Width = 1;
		out.m_Height = 1;
		return out;
	}

	public static Texture buildTexture(float[] pixels, Vector2f resolution, String name) {
		// GPU bound
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		// repeat texture
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// blur on stretch/shrink
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB4, 1, 1, 0, GL11.GL_RGBA, GL11.GL_FLOAT, pixels);

		Texture out = new Texture(name, textureID);
		out.m_Width = (int) resolution.x;
		out.m_Height = (int) resolution.y;
		return out;
	}

	private String m_Name;
	private int m_TextureID;
	private int m_Width;
	private int m_Height;

	public Texture(String file, boolean linearFilter) {
		this.name(file);
		s_Textures.put(file, this);

		// GPU bound
		m_TextureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_TextureID);

		// repeat texture
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// pixelate on stretch/shrink
		if (linearFilter) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		}

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = STBImage.stbi_load("res/textures/" + file + ".png", width, height, channels, 0);

		if (image != null) {
			image.flip();
			if (channels.get(0) == 4)
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(0), height.get(0), 0, GL11.GL_RGBA,
						GL11.GL_UNSIGNED_BYTE, image);
			else if (channels.get(0) == 3)
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width.get(0), height.get(0), 0, GL11.GL_RGB,
						GL11.GL_UNSIGNED_BYTE, image);
			else
				System.out.println("error in texture, unknown number of channels");
			m_Width = width.get(0);
			m_Height = height.get(0);
			STBImage.stbi_image_free(image);
		} else
			System.out.println("error in image loading: " + file);

	}

	public Texture(String file, int texID) {
		name(file);
		this.m_TextureID = texID;
		s_Textures.put(file, this);
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_TextureID);
	}

	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public int width() {
		return m_Width;
	}

	public int height() {
		return m_Height;
	}

	/**
	 * @return the file
	 */
	public String name() {
		return m_Name;
	}

	/**
	 * @param file the file to set
	 */
	public void name(String file) {
		this.m_Name = file;
	}

	/**
	 * @return the textureID
	 */
	public int textureID() {
		return m_TextureID;
	}

	public Texture copy() {
		Texture out = new Texture(m_Name, m_TextureID);
		out.m_Width = m_Width;
		out.m_Height = m_Height;
		return out;
	}

	@Override
	public String toString() {
		return string(0);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ",
				m_TextureID + ", (" + m_Width + ":" + m_Height + ")");
	}

}
