package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;

import engine.util.string.Stringable;

public abstract class Uniform implements Stringable {

	private String m_VariableName;
	private int m_Location;
	protected boolean m_WriteOut;

	public Uniform(String name) {
		m_VariableName = name;
		m_WriteOut = true;
	}

	public String name() {
		return m_VariableName;
	}

	public abstract void load();

	public void unload() {
	}

	public abstract Uniform clone();

	protected abstract String toWrite();

	public void location(int programID) {
		m_Location = GL20.glGetUniformLocation(programID, m_VariableName);
	}

	public int location() {
		return m_Location;
	}

	public void setWriteOut(boolean tf) {
		m_WriteOut = tf;
	}

	public String write() {
		if (m_WriteOut)
			return toWrite();
		return "";
	}

}
