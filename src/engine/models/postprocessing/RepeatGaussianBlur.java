package engine.models.postprocessing;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import engine.models.RawModel;
import engine.models.Texture;
import engine.shaders.Shader;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.Sampler2DUniform;

public class RepeatGaussianBlur extends PostProcessingEffect {

	private static final int MAX_LOD = 10;
	
	private List<GaussianBlur> m_Effects;
	
	private int m_LevelOfDetail;
	
	public RepeatGaussianBlur(Vector2f resolution, int levelOfDetail) {
		super();
		if (levelOfDetail > MAX_LOD || levelOfDetail < 1)
			throw new RuntimeException("Level of Detail must be 0 < lod < " + (MAX_LOD + 1));
		m_Effects = new ArrayList<>();
		m_LevelOfDetail = levelOfDetail;
		for (int i=0; i<MAX_LOD; i++)
			m_Effects.add(new GaussianBlur(resolution, i>levelOfDetail/2 ? 2 : 1));
	}

	@Override public void updateUniforms() {}

	@Override
	public Texture render(Object... args) {
		Texture image = (Texture) args[0];
		RawModel model = (RawModel) args[1];
		if (!enabled()) return image;
		for (int i = 0; i < m_LevelOfDetail; i++)
			image = m_Effects.get(i).render(image, model);
		return image;
	}
	
	@Override
	public void updateUniform(String name, Object value) {
		for (GaussianBlur blur : m_Effects)
			blur.updateUniform(name, value);
	}
	
	public void levelOfDetail(int lod) {
		m_LevelOfDetail = lod;
	}
	
	public int levelOfDetail() {
		return m_LevelOfDetail;
	}
	
}
