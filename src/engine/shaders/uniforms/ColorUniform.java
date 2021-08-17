package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;

import engine.util.Color;
import engine.util.string.StringTools;

public class ColorUniform extends Uniform {

	private Color m_Color;

	public ColorUniform(String name, Color color) {
		super(name);
		m_Color = color;
	}

	public void load() {
		GL20.glUniform3f(location(), m_Color.r(), m_Color.g(), m_Color.b());
	}

	public Color val() {
		return m_Color;
	}

	public ColorUniform val(Color color) {
		m_Color = color;
		return this;
	}

	@Override
	public Uniform clone() {
		return new ColorUniform(name(), new Color(m_Color));
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", m_Color.toString());
	}

	@Override
	public String toWrite() {
		return "ColorUniform;" + name() + ";" + m_Color.r() + "," + m_Color.g() + "," + m_Color.b();
	}

}
