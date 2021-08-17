package gameplay.targetPractice;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.Application;
import engine.animation.Timer;
import engine.components.renderable.gui.ProgressBar;
import engine.components.updateable.CameraController;
import engine.components.updateable.UpdateableComponent;
import engine.entities.Entity;
import engine.models.RayHit;
import engine.util.Color;
import engine.util.input.KeyboardPicker;
import engine.util.input.MousePicker;
import gameplay.targetPractice.guns.Gun;

public class Player extends UpdateableComponent {

	private Gun m_Gun;
	private TargetSpawner m_TargetSpawner;
	private Timer m_PrintTimer;

	private ProgressBar m_ScoreBar;

	@Override
	public void init() {
		super.init();
		entity().addComponent(new CameraController());
		m_Gun = new Gun();
		entity().addComponent(m_Gun);
		m_TargetSpawner = entity().layer().<TargetSpawner>component(TargetSpawner.class);
		transform().translate(new Vector3f(0, 0, 5));
		m_PrintTimer = new Timer(.1f);
		m_PrintTimer.finished((animatable, reversed) -> {
			float accuracy = m_Gun.shots() != 0 ? m_Gun.numHits() * 1f / m_Gun.shots() : 1;
			float hitPercentage = m_TargetSpawner.totalTargets() != 0
					? m_Gun.numHits() * 1f / m_TargetSpawner.totalTargets()
					: 1;
			float totalDist = 0;
			float totalLife = 0;
			for (RayHit hit : m_Gun.hits()) {
				totalDist += hit.distance();
				Target target = hit.hit().<Target>component(Target.class);
				totalLife += target.timeAlive();
			}
			float avDist = totalDist / m_Gun.numHits();
			float avLife = totalLife / m_Gun.numHits();

			float lifeScore = 1 - (avLife / m_TargetSpawner.targetLife());

			System.out.println("TOTAL TARGETS:  " + m_TargetSpawner.totalTargets());
			System.out.println("TOTAL SHOTS:    " + m_Gun.shots());
			System.out.println("TOTAL HITS:     " + m_Gun.numHits());
			System.out.println("ACCURACY:       " + accuracy);
			System.out.println("HIT PERCENTAGE: " + hitPercentage);
			System.out.println("AV HIT DIST:    " + avDist);
			System.out.println("AV LIFE SPAN:   " + avLife);
			System.out.println("TOTAL SCORE:    " + ((hitPercentage + accuracy + lifeScore) / 3f));
			System.out.println();
		});

		m_ScoreBar = new ProgressBar(1200, 50);
		m_ScoreBar.background().color(new Color("#987378"));
		m_ScoreBar.progressRect().color(new Color("#987378").mul(1.5f));
		m_ScoreBar.translate(new Vector2f(500, 50));

		Application.instance().layer("hud").addEntity(new Entity("score!").addComponent(m_ScoreBar));

	}

	private void printShots() {
		m_PrintTimer.start();
	}

	@Override
	public void update() {
		if (MousePicker.button(0))
			m_Gun.shoot(m_TargetSpawner.targets());
		if (KeyboardPicker.key(GLFW.GLFW_KEY_H))
			printShots();
		updateScoreBar();
	}

	private void updateScoreBar() {
		float accuracy = m_Gun.shots() != 0 ? m_Gun.numHits() * 1f / m_Gun.shots() : 1;
		float hitPercentage = m_TargetSpawner.totalTargets() != 0
				? m_Gun.numHits() * 1f / m_TargetSpawner.totalTargets()
				: 1;

		float score = (hitPercentage + accuracy) / 2f;
		m_ScoreBar.value(score);
	}

}
