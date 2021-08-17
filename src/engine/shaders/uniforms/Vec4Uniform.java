package engine.shaders.uniforms;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import engine.util.string.StringTools;

public class Vec4Uniform extends Uniform {

	private Vector4f m_Val;

	public Vec4Uniform(String name, Vector4f val) {
		super(name);
		m_Val = val;
	}

	public void load() {
		GL20.glUniform4f(location(), m_Val.x(), m_Val.y(), m_Val.z(), m_Val.w());
	}

	public Vec4Uniform val(Vector4f val) {
		m_Val = val;
		return this;
	}

	public Vector4f val() {
		return m_Val;
	}

	@Override
	public Uniform clone() {
		return new Vec4Uniform(name(), m_Val);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", m_Val.toString());
	}

	@Override
	public String toWrite() {
		return "Vec4Uniform;" + name() + ";" + m_Val.x() + "," + m_Val.y() + "," + m_Val.z() + "," + m_Val.w();
	}

}
