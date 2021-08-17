package betrayal.controllers;

import org.joml.Vector2f;

import engine.Material;
import engine.animation.FloatAnimation;
import engine.animation.GroupAnimation;
import engine.components.renderable.gui.Button;
import engine.components.renderable.gui.FullscreenRectangle;
import engine.components.renderable.gui.Rectangle;
import engine.entities.Entity;
import engine.models.Texture;
import engine.util.Color;

public class MainMenuController extends BetrayalController {

	@Override
	public void showMenu() {
		m_ShowMenuAnimations.reversed(false);
		m_ShowMenuAnimations.play();
		entity().layer().enable();
	}

	@Override
	public void hideMenu() {
		m_ShowMenuAnimations.reversed(true);
		m_ShowMenuAnimations.play();
	}

	private GroupAnimation m_ShowMenuAnimations;

	@Override
	public void init() {

		FullscreenRectangle background = new FullscreenRectangle(entity().layer().window().resolution());
		background.rect().material(Material.material("menu/transparent"));
		entity().layer().addEntity(new Entity("background").addComponent(background));

		Rectangle title = new Rectangle(1000, 500);
		title.rect().material(Material.material("menu/title"));
		title.screenTranslate(new Vector2f(.5f, .1f), entity().layer().window().resolution());
		title.image(new Texture("title", true));
		entity().layer().addEntity(new Entity("title").addComponent(title));

		Button playButton = new Button(400, 200);
		playButton.rect().material(Material.material("menu/playButton"));
		playButton.screenTranslate(new Vector2f(0.1f, .5f), entity().layer().window().resolution());
		playButton.image(new Texture("playButton", true));
		playButton.borderColor(Color.parse("#223366"));
		playButton.hover((obj, vec2) -> playButton.borderWidth(2));
		playButton.unhover((obj, vec2) -> playButton.borderWidth(0));
//		playButton.press((obj, btn, pos) -> ls.showHideMenu());
		entity().layer().addEntity(new Entity("playButton").addComponent(playButton));

		m_ShowMenuAnimations = new GroupAnimation();
		m_ShowMenuAnimations.length(.2f);
		m_ShowMenuAnimations.finished((obj, rev) -> {
			if (rev)
				entity().layer().disable();
		});
		m_ShowMenuAnimations.addAnimation(new FloatAnimation(title, .2f, "opacity", 0, 1));
		m_ShowMenuAnimations.addAnimation(new FloatAnimation(playButton, .2f, "opacity", 0, 1));

	}

	@Override
	public void update() {

	}

}
