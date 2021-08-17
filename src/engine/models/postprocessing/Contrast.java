package engine.models.postprocessing;

import org.joml.Vector2f;

import engine.shaders.Shader;
import engine.shaders.uniforms.FloatUniform;

public class Contrast extends PostProcessingEffect {

	private float m_Contrast;
	
	public Contrast(float contrast, Vector2f resolution) {
		super(Shader.shader("contrastPPE"), resolution);
		m_Contrast = contrast;
	}

	/**
	 * @return the contrast
	 */
	public float contrast() {
		return m_Contrast;
	}

	/**
	 * @param contrast the contrast to set
	 */
	public Contrast contrast(float contrast) {
		m_Contrast = contrast;
		return this;
	}

	@Override
	public void updateUniforms() {
		m_Shader.<FloatUniform>uniform("contrast").val(m_Contrast);
	}
	
}
