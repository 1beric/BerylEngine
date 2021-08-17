package betrayal.controllers;

import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import betrayal.cards.Deck;
import betrayal.cards.EventCard;
import betrayal.cards.ItemCard;
import betrayal.cards.OmenCard;
import betrayal.player.Player;
import betrayal.tiles.Tile;
import engine.Layer;
import engine.Material;
import engine.animation.Animation;
import engine.animation.CustomAnimation;
import engine.components.renderable.Camera;
import engine.components.renderable.Mesh3;
import engine.components.renderable.PointLight;
import engine.components.renderable.PostProcessor;
import engine.components.renderable.Skybox;
import engine.components.renderable.Transform;
import engine.components.updateable.CameraController;
import engine.entities.Entity;
import engine.events.keyboard.KeyPressedEvent;
import engine.models.Cubemap;
import engine.models.creation.PlaneBuilder;
import engine.models.creation.ProceduralSkybox;
import engine.models.postprocessing.RepeatGaussianBlur;
import engine.util.Color;

public class GameController extends BetrayalController {

	private Vector3f startingPosition = new Vector3f(-0.376f, 4.145f, 2.81f);
	private Vector3f startingRotation = new Vector3f(67.074f, -0.427f, 0f);

	private Animation blurAnim;
	private RepeatGaussianBlur blur;

	private Deck<OmenCard> m_OmenCards;
	private Deck<EventCard> m_EventCards;
	private Deck<ItemCard> m_ItemCards;
	private Deck<Tile> m_HouseTiles;
	private List<Player> m_Players;

	private CameraController m_CameraController;

	@Override
	public void init() {
		setupEnvironment();

		m_OmenCards = createOmenCards();
		m_ItemCards = createItemCards();
		m_EventCards = createEventCards();

		m_HouseTiles = createHouseTilesDeck();

		setupPlayers();
//		m_SmallMonsters = createSmallMonsters();
//		m_LargeMonsters = createLargeMonsters();

		m_CameraController.controlling(false);

	}

	private void setupPlayers() {
		m_Players = Player.createPlayers();

		int i = 0;
		for (Player p : m_Players) {
			Entity pEnt = new Entity(p.name(), new Vector3f((i++ - m_Players.size() / 2) * 5, 0, 0));
			pEnt.addComponent(p);
			entity().layer().addEntity(pEnt);
//			System.out.println(StringTools.smartToString(p));
		}
	}

	private Deck<OmenCard> createOmenCards() {
		// TODO Auto-generated method stub
		return null;
	}

	private Deck<ItemCard> createItemCards() {
		// TODO Auto-generated method stub
		return null;
	}

	private Deck<EventCard> createEventCards() {
		// TODO Auto-generated method stub
		return null;
	}

	private Deck<Tile> createHouseTilesDeck() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setupEnvironment() {
		Layer gameLayer = entity().layer();

		Skybox skybox = new Skybox();
		skybox.uniform("cubemap1", ProceduralSkybox.generateMap(new Vector2f(512), "sky!"));
		skybox.uniform("cubemap1", Cubemap.create("blue", Color.blue().interpolate(.7f, Color.white())));
		entity().layer().addEntity(new Entity("skybox").addComponent(skybox));

		gameLayer.removeEntity("Main Light");
		gameLayer.addEntity(new Entity("light", new Vector3f(0, 3, 0))
				.addComponent(new PointLight().color(new Color(.9f, .94f, .85f))));

		entity().layer().<Camera>component(Camera.class).entity().addComponent(new CameraController());
		entity().layer().<Camera>component(Camera.class).entity().<Transform>component(Transform.class)
				.position(startingPosition);
		entity().layer().<Camera>component(Camera.class).entity().<Transform>component(Transform.class)
				.rotation(startingRotation);

		m_CameraController = entity().layer().<CameraController>component(CameraController.class);

		Mesh3 floor = new Mesh3(PlaneBuilder.build(false));
		floor.material(Material.material("floor"));
		Entity floorEnt = new Entity("floor", new Vector3f(0, -1f, 0), new Vector3f(0), new Vector3f(100))
				.addComponent(floor);

		gameLayer.addEntity(floorEnt);

		PostProcessor pp = new PostProcessor();
		blur = new RepeatGaussianBlur(entity().layer().window().resolution(), 9);
		pp.addPPE(blur);
		blurAnim = new CustomAnimation(.2f, (obj, factor) -> {
			blur.levelOfDetail((int) (1 + factor * 4));
		});
		blurAnim.finished((obj, rev) -> {
			if (rev)
				blur.disable();
		});

		entity().layer().addEntity(new Entity("post Processing").addComponent(pp));

	}

	@Override
	public void showMenu() {
		blur.enable();
		blurAnim.reversed(false);
		blurAnim.start();
		m_CameraController.controlling(false);
		disable();
	}

	@Override
	public void hideMenu() {
		blurAnim.reversed(true);
		blurAnim.start();
		m_CameraController.controlling(true);
		enable();
	}

	@Override
	public boolean handleKeyPressed(KeyPressedEvent kpe) {
		if (!enabled())
			return false;
		return false;
	}

}
