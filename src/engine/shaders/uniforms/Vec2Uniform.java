package engine.shaders.uniforms;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

import engine.util.string.StringTools;

public class Vec2Uniform extends Uniform {

	private Vector2f m_Val;

	public Vec2Uniform(String name, Vector2f val) {
		super(name);
		m_Val = val;
	}

	public void load() {
		GL20.glUniform2f(location(), m_Val.x(), m_Val.y());
	}

	public Vec2Uniform val(Vector2f val) {
		m_Val = val;
		return this;
	}

	public Vector2f val() {
		return m_Val;
	}

	@Override
	public Uniform clone() {
		return new Vec2Uniform(name(), m_Val);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", m_Val.toString());
	}

	@Override
	public String toWrite() {
		return "Vec2Uniform;" + name() + ";" + m_Val.x() + "," + m_Val.y();
	}

}
