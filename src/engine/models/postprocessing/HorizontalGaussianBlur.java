package engine.models.postprocessing;

import org.joml.Vector2f;

import engine.shaders.Shader;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.Vec2Uniform;

public class HorizontalGaussianBlur extends PostProcessingEffect {

	public HorizontalGaussianBlur(Vector2f resolution, float scale) {
		super(Shader.shader("horizontalGaussianBlurPPE"), resolution, scale);
		m_Shader.<FloatUniform>uniform("widthInPixels").val(resolution.x);
	}

	@Override public void updateUniforms() {}
}
