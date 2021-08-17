package engine.models.postprocessing;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import engine.animation.Animatable;
import engine.components.renderable.PostProcessor;
import engine.models.FrameBuffer;
import engine.models.ModelData;
import engine.models.RawModel;
import engine.models.FrameBuffer.DepthType;
import engine.models.Texture;
import engine.shaders.Shader;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.MatrixUniform;
import engine.shaders.uniforms.Sampler2DUniform;
import engine.shaders.uniforms.Uniform;
import engine.util.Enableable;
import engine.util.Renderable;

public abstract class PostProcessingEffect implements Renderable, Animatable, Enableable {

	private FrameBuffer m_FBO;
	protected Shader m_Shader;
	private PostProcessor m_PostProcessor;
	
	protected boolean m_Enabled;

	
	public PostProcessingEffect(Shader shader, Vector2f resolution, float scale) {
		m_FBO = new FrameBuffer(resolution, scale, DepthType.DEPTH_RENDER_BUFFER);
		m_Shader = shader;
		m_Enabled = true;
	}
	
	public PostProcessingEffect(Shader shader, Vector2f resolution) {
		m_FBO = new FrameBuffer(resolution, DepthType.DEPTH_RENDER_BUFFER, false);
		m_Shader = shader;
		m_Enabled = true;
	}
	
	public PostProcessingEffect() {
		// this is used for ppes that just hold other ppes.
		m_Enabled = true;
	}
	
	public abstract void updateUniforms();
	
	public PostProcessingEffect postProcessor(PostProcessor pp) {
		m_PostProcessor = pp;
		return this;
	}
	
	public PostProcessor postProcessor() {
		return m_PostProcessor;
	}
	
	@Override
	public Texture render(Object... args) {
		Texture image = (Texture) args[0];
		RawModel model = (RawModel) args[1];
		if (!enabled()) return image;
		start();
		model.bind();
		m_Shader.start();
		image.bind();
		m_Shader.<Sampler2DUniform>uniform("tex").val(image);
		updateUniforms();
		m_Shader.loadUniforms();
		m_FBO.bind();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		model.draw();
		m_FBO.unbind();
		image.unbind();
		m_Shader.stop();
		model.unbind();
		return m_FBO.texture1();
	}
	
	private void start() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glFrontFace(GL11.GL_CW);
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return m_Shader.<T>uniform(name);
	}
	
	public boolean enabled() {
		return m_Enabled;
	}
	
	public void enable() {
		m_Enabled = true;
	}

	public void disable() {
		m_Enabled = false;
	}

	
}
