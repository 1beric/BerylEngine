package engine.rendering;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import engine.Layer;
import engine.components.renderable.Camera;
import engine.components.renderable.Skybox;
import engine.models.ModelData;
import engine.models.RawModel;
import engine.util.Loader;

public class SkyboxRenderer {

	private static final float s_SIZE = 1024;
	
	public static RawModel s_CUBE;
	
	public static void render(Layer layer) {
		start();

		Skybox skybox = layer.<Skybox>component(Skybox.class);
		if (skybox == null) return;
		Vector2f resolution = layer.window().resolution();
        float aspect = resolution.x / resolution.y;
		skybox.render(layer.<Camera>component(Camera.class), aspect, s_CUBE);
	}
	
	private static void start() {
		if (s_CUBE == null) s_CUBE = Loader.loadToVAO(new ModelData(VERTICES, 3));
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glFrontFace(GL11.GL_CW);
	}

	private static final float[] VERTICES = {        
			-s_SIZE,  s_SIZE, -s_SIZE,
			s_SIZE,  s_SIZE, -s_SIZE,
			s_SIZE, -s_SIZE, -s_SIZE,
			s_SIZE, -s_SIZE, -s_SIZE,			// NEG Z
			-s_SIZE,  s_SIZE, -s_SIZE,
			-s_SIZE, -s_SIZE, -s_SIZE,
			
			-s_SIZE, -s_SIZE,  s_SIZE,
			s_SIZE, -s_SIZE,  s_SIZE,
			s_SIZE,  s_SIZE,  s_SIZE,
			s_SIZE,  s_SIZE,  s_SIZE,			// POS Z
			-s_SIZE, -s_SIZE,  s_SIZE,
			-s_SIZE,  s_SIZE,  s_SIZE,
			
			-s_SIZE, -s_SIZE,  s_SIZE,
			-s_SIZE,  s_SIZE,  s_SIZE,
			-s_SIZE,  s_SIZE, -s_SIZE,
			-s_SIZE,  s_SIZE, -s_SIZE,			// NEG X
			-s_SIZE, -s_SIZE,  s_SIZE,
			-s_SIZE, -s_SIZE, -s_SIZE,
			
			s_SIZE, -s_SIZE, -s_SIZE,
			s_SIZE,  s_SIZE, -s_SIZE,
			s_SIZE,  s_SIZE,  s_SIZE,
			s_SIZE,  s_SIZE,  s_SIZE,			// POS X
			s_SIZE, -s_SIZE, -s_SIZE,
			s_SIZE, -s_SIZE,  s_SIZE,
			
			s_SIZE, -s_SIZE,  s_SIZE,
			-s_SIZE, -s_SIZE,  s_SIZE,
			s_SIZE, -s_SIZE, -s_SIZE,
			s_SIZE, -s_SIZE, -s_SIZE,			// NEG Y
			-s_SIZE, -s_SIZE, -s_SIZE,
			-s_SIZE, -s_SIZE,  s_SIZE,
			
			-s_SIZE,  s_SIZE, -s_SIZE,
			-s_SIZE,  s_SIZE,  s_SIZE,
			s_SIZE,  s_SIZE,  s_SIZE,
			s_SIZE,  s_SIZE,  s_SIZE,			// POS Y
			-s_SIZE,  s_SIZE, -s_SIZE,
			s_SIZE,  s_SIZE, -s_SIZE,
		};
	
	

}
