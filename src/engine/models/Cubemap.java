package engine.models;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.util.Color;
import engine.util.Loader;

public class Cubemap {

	public static Cubemap defaultMap() {
		return new Cubemap("default",
				new float[][] { new float[] { 0, 0, 0, 1 }, new float[] { 0, 0, 0, 1 }, new float[] { 0, 0, 0, 1 },
						new float[] { 0, 0, 0, 1 }, new float[] { 0, 0, 0, 1 }, new float[] { 0, 0, 0, 1 }, },
				new Vector2f(1));
	}

	public static Cubemap create(String name, Color color) {
		return new Cubemap(name, new float[][] { new float[] { color.r(), color.g(), color.b(), 1 },
				new float[] { color.r(), color.g(), color.b(), 1 }, new float[] { color.r(), color.g(), color.b(), 1 },
				new float[] { color.r(), color.g(), color.b(), 1 }, new float[] { color.r(), color.g(), color.b(), 1 },
				new float[] { color.r(), color.g(), color.b(), 1 }, }, new Vector2f(1));
	}

	private String m_Name;
	private int m_ID;

	public Cubemap(String name, float[][] pixels, Vector2f resolution) {
		name(name);
		m_ID = Loader.loadCubemap(pixels, resolution);
	}

	public Cubemap(String name) {
		name(name);
		m_ID = Loader.loadCubemap(name);
	}

	public void bind() {
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, m_ID);
	}

	public void unbind() {
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
	}

	/**
	 * @return m_Name
	 */
	public String name() {
		return m_Name;
	}

	/**
	 * @param m_Name m_Name to set
	 */
	public void name(String m_Name) {
		this.m_Name = m_Name;
	}

}
