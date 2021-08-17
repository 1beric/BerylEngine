package engine.rendering;

import org.lwjgl.opengl.GL11;

import engine.models.ModelData;
import engine.models.RawModel;
import engine.models.Texture;
import engine.shaders.Shader;
import engine.util.Color;
import engine.util.Loader;

public class TextRenderer {

	public static RawModel s_TL;
	private static Shader s_Text;

	public static void render(Texture m_Texture, float drawX, float drawY, int x, int y, int width, int height,
			Color c) {

	}

	public static void render(Texture background) {
		if (background == null)
			return;
		start();
		GL11.glFrontFace(GL11.GL_CW);
//		s_SCREEN.bind();
//		s_SimpleImage.start();
//		s_SimpleImage.<Sampler2DUniform>uniform("tex").val(background);
//		s_SimpleImage.loadUniforms();
//		s_SCREEN.draw();
//		s_SimpleImage.unloadUniforms();
//		s_SimpleImage.stop();
//		s_SCREEN.unbind();
	}

	private static void start() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glFrontFace(GL11.GL_CCW);
		if (s_TL == null)
			s_TL = Loader.loadToVAO(new ModelData(new float[] { 0, 0, 0, 1, 1, 0, 1, 1 }, 2));
		if (s_Text == null)
			s_Text = Shader.shader("text");
	}

}
