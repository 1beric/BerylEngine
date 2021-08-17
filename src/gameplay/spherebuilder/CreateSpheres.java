package gameplay.spherebuilder;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.Application;
import engine.Layer;
import engine.components.renderable.Mesh3;
import engine.components.renderable.Skybox;
import engine.components.updateable.CameraController;
import engine.components.updateable.UpdateableComponent;
import engine.entities.Entity;
import engine.events.keyboard.KeyPressedEvent;
import engine.models.creation.ProceduralSkybox;
import engine.models.creation.SphereBuilder;
import engine.rendering.RenderType;

public class CreateSpheres extends UpdateableComponent {

	private CameraController m_CameraController;

	@Override
	public void init() {
		Layer primary = new Layer("primary", Application.instance().primaryWindow(), RenderType.MODEL);

		Skybox skybox = new Skybox();
		skybox.uniform("cubemap1", ProceduralSkybox.generateMap(new Vector2f(512), "sky!"));
		primary.addEntity(new Entity("skybox").addComponent(skybox));

		primary.entity("Main Camera").addComponent(m_CameraController = new CameraController().controlling(true));

		for (int i = 1; i <= 20; i++) {
			Entity sphere = new Entity("Sphere " + i, new Vector3f((i - 10) * 4, 0, 0));
			Mesh3 mesh = new Mesh3(SphereBuilder.build(i));
//			mesh.uniform("color", Color.rand());
			sphere.addComponent(mesh);
			primary.addEntity(sphere);
		}
		Application.instance().pushLayer(primary);
	}

	@Override
	public boolean handleKeyPressed(KeyPressedEvent kpe) {
		if (kpe.key() == GLFW.GLFW_KEY_ESCAPE) {
			m_CameraController.controlling(false);
			return true;
		}
		return super.handleKeyPressed(kpe);
	}

}
