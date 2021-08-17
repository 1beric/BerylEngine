package engine.models.postprocessing;

import org.joml.Vector2f;

import engine.shaders.Shader;
import engine.shaders.uniforms.FloatUniform;

public class Brightness extends PostProcessingEffect {

	private float m_Brightness;
	
	public Brightness(float brightness, Vector2f resolution) {
		super(Shader.shader("brightnessPPE"), resolution);
		m_Brightness = brightness;
	}

	/**
	 * @return the contrast
	 */
	public float brightness() {
		return m_Brightness;
	}

	/**
	 * @param contrast the contrast to set
	 */
	public Brightness brightness(float brightness) {
		m_Brightness = brightness;
		return this;
	}

	@Override
	public void updateUniforms() {
		m_Shader.<FloatUniform>uniform("brightness").val(m_Brightness);
	}
	
}
