package engine.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;

import engine.Layer;
import engine.models.Texture;

public class MasterRenderer {

	private static final boolean USE_FBOS = true;

	public static void render(List<Layer> layers) {
		GL11.glClearColor(1f, 0, 1f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		if (USE_FBOS)
			renderFBO(layers);
		else
			renderNoFBO(layers);
	}

	private static void renderNoFBO(List<Layer> layers) {
		for (Layer layer : layers) {
			if (layer.is2D())
				GuiRenderer.render(layer);
			else {
				ModelRenderer.render(layer);
				ModelRenderer.renderTransforms(layer);
			}
		}
	}

	private static void renderFBO(List<Layer> layers) {
		Texture image = null;
		for (Layer layer : layers)
			image = layer.render(image);
		GuiRenderer.render(image);
	}

}
