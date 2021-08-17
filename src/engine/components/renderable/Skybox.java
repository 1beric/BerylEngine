package engine.components.renderable;

import org.joml.Matrix4f;

import engine.animation.Animatable;
import engine.models.RawModel;
import engine.models.Texture;
import engine.rendering.ModelRenderer;
import engine.shaders.Shader;
import engine.shaders.uniforms.MatrixUniform;
import engine.shaders.uniforms.Uniform;

public class Skybox extends RenderableComponent implements Animatable {

	private Shader m_Shader;

	public Skybox() {
		m_Shader = Shader.shader("skybox");
	}

	@Override
	public Texture render(Object... args) {
		Camera cam = (Camera) args[0];
		float aspect = (float) args[1];
		RawModel model = (RawModel) args[2];
		model.bind();
		m_Shader.start();
		Matrix4f view = cam.viewMatrix();
		view.m30(0);
		view.m31(0);
		view.m32(0);
		m_Shader.<MatrixUniform>uniform("viewMatrix").val(view);
		m_Shader.<MatrixUniform>uniform("projectionMatrix").val(ModelRenderer.perspective(aspect));
		m_Shader.loadUniforms();
		model.draw();
		m_Shader.stop();
		model.unbind();
		return null;
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return m_Shader.<T>uniform(name);
	}

}
