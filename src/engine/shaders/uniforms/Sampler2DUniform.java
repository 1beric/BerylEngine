package engine.shaders.uniforms;

import org.lwjgl.opengl.GL13;

import engine.models.Texture;
import engine.util.string.StringTools;

public class Sampler2DUniform extends Uniform {

	private int m_TexSlot;
	private Texture m_Tex;

	public Sampler2DUniform(String name, Texture tex, int texSlot) {
		super(name);
		m_Tex = tex;
		m_TexSlot = texSlot;
	}

	@Override
	public void load() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + m_TexSlot);
		m_Tex.bind();
	}

	@Override
	public void unload() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + m_TexSlot);
		m_Tex.unbind();
	}

	@Override
	public void location(int programID) {
	}

	public Texture val() {
		return m_Tex;
	}

	public Sampler2DUniform val(Texture tex) {
		m_Tex = tex;
		return this;
	}

	@Override
	public Uniform clone() {
		return new Sampler2DUniform(name(), m_Tex, m_TexSlot);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", m_Tex.toString());
	}

	@Override
	public String toWrite() {
		return "Sampler2DUniform;" + name() + ";" + m_Tex.name() + "," + m_TexSlot;
	}

}
