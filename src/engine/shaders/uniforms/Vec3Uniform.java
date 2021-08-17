package engine.shaders.uniforms;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import engine.util.string.StringTools;

public class Vec3Uniform extends Uniform {

	private Vector3f m_Val;

	public Vec3Uniform(String name, Vector3f val) {
		super(name);
		m_Val = val;
	}

	public void load() {
		GL20.glUniform3f(location(), m_Val.x(), m_Val.y(), m_Val.z());
	}

	public Vec3Uniform val(Vector3f val) {
		m_Val = val;
		return this;
	}

	public Vector3f val() {
		return m_Val;
	}

	@Override
	public Uniform clone() {
		return new Vec3Uniform(name(), m_Val);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", m_Val.toString());
	}

	@Override
	public String toWrite() {
		return "Vec3Uniform;" + name() + ";" + m_Val.x() + "," + m_Val.y() + "," + m_Val.z();
	}

}
