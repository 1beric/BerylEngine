package engine.shaders.uniforms;

import org.lwjgl.opengl.GL13;

import engine.models.Cubemap;
import engine.util.string.StringTools;

public class SamplerCubeUniform extends Uniform {

	private int m_TexSlot;
	private Cubemap m_Map;

	public SamplerCubeUniform(String name, Cubemap map, int texSlot) {
		super(name);
		m_Map = map;
		m_TexSlot = texSlot;
	}

	@Override
	public void load() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + m_TexSlot);
		m_Map.bind();
	}

	@Override
	public void unload() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + m_TexSlot);
		m_Map.unbind();
	}

	@Override
	public void location(int programID) {
	}

	public Cubemap val() {
		return m_Map;
	}

	public SamplerCubeUniform val(Cubemap map) {
		m_Map = map;
		return this;
	}

	@Override
	public Uniform clone() {
		return new SamplerCubeUniform(name(), m_Map, m_TexSlot);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), ": ", m_Map.toString());
	}

	@Override
	public String toWrite() {
		return "SamplerCubeUniform;" + name() + ";" + m_Map.name() + "," + m_TexSlot;
	}

}
