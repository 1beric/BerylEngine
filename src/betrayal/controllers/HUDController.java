package betrayal.controllers;

import engine.Material;
import engine.components.renderable.gui.Rectangle;
import engine.entities.Entity;
import engine.models.Texture;
import engine.util.input.Crosshair;

public class HUDController extends BetrayalController {

	@Override
	public void init() {
//		entity().layer().addEntity(new Entity("debug").addComponent(new DebugTester()));

		Rectangle crosshair = new Rectangle(16, 16);
		crosshair.rect().material(Material.material("crosshair"));

		crosshair.translate(entity().layer().window().resolution().mul(.5f).sub(8, 8));
		crosshair.image(new Texture(Crosshair.LARGE_DIAMOND_DISCONNECTED_CENTER.path(), false));
		entity().layer().addEntity(new Entity("crosshair center").addComponent(crosshair));

	}

	@Override
	public void showMenu() {
		entity().layer().disable();
	}

	@Override
	public void hideMenu() {
		entity().layer().enable();
	}

}
