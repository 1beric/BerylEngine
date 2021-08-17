package gameplay.targetPractice;

import org.joml.Vector2f;

import engine.Application;
import engine.Layer;
import engine.components.renderable.Skybox;
import engine.components.renderable.gui.Rectangle;
import engine.components.updateable.UpdateableComponent;
import engine.entities.Entity;
import engine.models.creation.ProceduralSkybox;
import engine.rendering.RenderType;

public class TargetPractice extends UpdateableComponent {

	@Override
	public void init() {
		Layer primary = new Layer("primary", Application.instance().primaryWindow(), RenderType.MODEL);

		Skybox skybox = new Skybox();
		skybox.uniform("cubemap1", ProceduralSkybox.generateMap(new Vector2f(512), "sky!"));
		primary.addEntity(new Entity("skybox").addComponent(skybox));

		primary.entity("Main Camera").addComponent(new Player());

		primary.addEntity(new Entity("target manager").addComponent(new TargetSpawner()));

		Application.instance().pushLayer(primary);

		Layer hud = new Layer("hud", Application.instance().primaryWindow(), RenderType.GUI);

		int chSize = 6;

		Rectangle crosshair = new Rectangle(chSize, chSize);
		crosshair.translate(Application.instance().primaryWindow().resolution().mul(.5f, new Vector2f()).sub(chSize / 2,
				chSize / 2));
//		crosshair.image(new Texture(Crosshair.RING_CENTER.path(), false));
		hud.addEntity(new Entity("crosshair").addComponent(crosshair));

		Application.instance().pushLayer(hud);
	}

}
