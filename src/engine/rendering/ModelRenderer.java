package engine.rendering;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import engine.Layer;
import engine.components.renderable.Camera;
import engine.components.renderable.Light;
import engine.components.renderable.Mesh3;
import engine.components.renderable.Transform;
import engine.entities.Entity;

public class ModelRenderer {

	private static final float FOV = (float) (Math.PI / 2f);
	private static final float PERSPECTIVE_NEAR = 0.001f;
	private static final float PERSPECTIVE_FAR = 5000;	
	
	public static void render(Layer layer) {
		start();
        float aspect = layer.window().width() / layer.window().height();

		Camera cam = layer.<Camera>component(Camera.class);
		Light light = layer.<Light>component(Light.class);
		Matrix4f projection = new Matrix4f().identity().perspective(FOV, aspect, PERSPECTIVE_NEAR, PERSPECTIVE_FAR);

		for (Entity entity : layer.entities()) {
			Mesh3 mesh = (Mesh3)entity.component(Mesh3.class);
			if (mesh == null) continue;
			mesh.render(cam, light, projection);
		}
	}
	
	public static void renderTransforms(Layer layer) {
		start();
        float aspect = layer.window().width() / layer.window().height();

        Camera cam = layer.<Camera>component(Camera.class);
		Light light = layer.<Light>component(Light.class);
		Matrix4f projection = perspective(aspect);
		
		Entity selected = layer.selected();
		if (selected != null) selected.<Transform>component(Transform.class).render(cam, light, projection);
		
//		for (Entity entity : layer.entities())
//			entity.<Transform>component(Transform.class).render(cam, light, projection);
	}
	
	private static void start() {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glFrontFace(GL11.GL_CW);
	}
	
	public static Matrix4f perspective(float aspect) {
		return new Matrix4f().identity().perspective(FOV, aspect, PERSPECTIVE_NEAR, PERSPECTIVE_FAR);
	}
	
}
