package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;

import engine.util.string.StringTools;

public class FloatUniform extends Uniform {

	private float m_Val;

	public FloatUniform(String name, float val) {
		super(name);
		m_Val = val;
	}

	public void load() {
		GL20.glUniform1f(location(), m_Val);
	}

	public FloatUniform val(float val) {
		m_Val = val;
		return this;
	}

	public float val() {
		return m_Val;
	}

	@Override
	public Uniform clone() {
		return new FloatUniform(name(), m_Val);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", "" + m_Val);
	}

	@Override
	public String toWrite() {
		return "FloatUniform;" + name() + ";" + m_Val;
	}

}
