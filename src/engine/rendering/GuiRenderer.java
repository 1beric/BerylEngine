package engine.rendering;

import org.lwjgl.opengl.GL11;

import engine.Layer;
import engine.components.renderable.Mesh2;
import engine.entities.Entity;
import engine.models.ModelData;
import engine.models.RawModel;
import engine.models.Texture;
import engine.shaders.Shader;
import engine.shaders.uniforms.Sampler2DUniform;
import engine.util.Loader;

public class GuiRenderer {

	public static RawModel s_TL;
	private static RawModel s_SCREEN;
	private static Shader s_SimpleImage;

	public static RawModel screen() {
		if (s_SCREEN == null)
			s_SCREEN = Loader.loadToVAO(new ModelData(new float[] { -1, -1, -1, 1, 1, -1, 1, 1 }, 2));
		return s_SCREEN;
	}

	public static void render(Layer layer) {
		start();
		s_TL.bind();
		for (Entity entity : layer.entities()) {
//			if (!entity.enabled()) continue;
			Mesh2 mesh = (Mesh2) entity.component(Mesh2.class);
			if (mesh == null || !mesh.enabled())
				continue;
			mesh.render(layer.window().resolution(), s_TL);
		}
		s_TL.unbind();
	}

	public static void render(Texture background) {
		if (background == null)
			return;
		start();
		GL11.glFrontFace(GL11.GL_CW);
		s_SCREEN.bind();
		s_SimpleImage.start();
		s_SimpleImage.<Sampler2DUniform>uniform("tex").val(background);
		s_SimpleImage.loadUniforms();
		s_SCREEN.draw();
		s_SimpleImage.unloadUniforms();
		s_SimpleImage.stop();
		s_SCREEN.unbind();
	}

	private static void start() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glFrontFace(GL11.GL_CCW);
		if (s_TL == null)
			s_TL = Loader.loadToVAO(new ModelData(new float[] { 0, 0, 0, 1, 1, 0, 1, 1 }, 2));
		if (s_SCREEN == null)
			s_SCREEN = Loader.loadToVAO(new ModelData(new float[] { -1, -1, -1, 1, 1, -1, 1, 1 }, 2));
		if (s_SimpleImage == null)
			s_SimpleImage = Shader.shader("simpleImage");
	}

}
