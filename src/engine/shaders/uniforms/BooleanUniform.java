package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;

import engine.util.string.StringTools;

public class BooleanUniform extends Uniform {

	private boolean m_Val;

	public BooleanUniform(String name, boolean val) {
		super(name);
		m_Val = val;
	}

	public void load() {
		GL20.glUniform1f(location(), m_Val ? 1 : 0);
	}

	public BooleanUniform val(boolean val) {
		m_Val = val;
		return this;
	}

	public boolean val() {
		return m_Val;
	}

	@Override
	public Uniform clone() {
		return new BooleanUniform(name(), m_Val);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", "" + m_Val);
	}

	@Override
	public String toWrite() {
		return "BooleanUniform;" + name() + ";" + m_Val;
	}

}
