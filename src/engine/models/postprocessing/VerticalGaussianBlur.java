package engine.models.postprocessing;

import org.joml.Vector2f;

import engine.shaders.Shader;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.Vec2Uniform;

public class VerticalGaussianBlur extends PostProcessingEffect {

	public VerticalGaussianBlur(Vector2f resolution, float scale) {
		super(Shader.shader("verticalGaussianBlurPPE"), resolution, scale);
		m_Shader.<FloatUniform>uniform("heightInPixels").val(resolution.y);
	}

	@Override public void updateUniforms() {}
}
