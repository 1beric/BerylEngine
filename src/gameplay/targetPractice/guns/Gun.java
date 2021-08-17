package gameplay.targetPractice.guns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.Application;
import engine.animation.Animatable;
import engine.animation.Animation;
import engine.animation.Timer;
import engine.components.renderable.Camera;
import engine.components.renderable.Collider;
import engine.components.renderable.Transform;
import engine.components.renderable.gui.ProgressBar;
import engine.components.updateable.UpdateableComponent;
import engine.entities.Entity;
import engine.models.RayHit;
import engine.util.Color;
import gameplay.targetPractice.Target;

public class Gun extends UpdateableComponent {

	private float m_ShotDelay = 1 / 2.5f;
	private boolean m_AllowShot = true;

	private int m_Shots;
	private List<RayHit> m_Hits;

	private Timer m_Timer;

	private Transform m_CamTransform;

	private ProgressBar m_ProgressBar;
	private Animation m_ProgressBarAnim;

	@Override
	public void init() {
		m_Timer = new Timer(m_ShotDelay);
		m_Timer.finished((animatable, reversed) -> m_AllowShot = true);

		m_CamTransform = entity().<Camera>component(Camera.class).transform();

		m_Hits = new ArrayList<>();
		shots(0);

		m_ProgressBar = new ProgressBar(200, 50);
		m_ProgressBar.background().color(new Color("#738678"));
		m_ProgressBar.progressRect().color(new Color("#738678").mul(1.5f));
		m_ProgressBar.translate(new Vector2f(100, 50));

		m_ProgressBarAnim = new Animation(m_ProgressBar, m_ShotDelay) {
			@Override
			public void process(float factor, Animatable animatable) {
				m_ProgressBar.value(factor);
			}
		};

		Application.instance().layer("hud").addEntity(new Entity("gun progress").addComponent(m_ProgressBar));
	}

	public void shoot(Collection<Target> targets) {
		if (!m_AllowShot)
			return;

		Vector3f origin = transform().position();
		Vector3f direction = transform().forward();

		RayHit currentClose = Collider.castDefault(m_CamTransform.position(), m_CamTransform.forward());

		for (Target target : targets) {
			RayHit current = target.collides(origin, direction);
			if (current.hit() != null && current.hit().name().equals(entity().name()))
				continue;
			if (current.distance() < currentClose.distance())
				currentClose = current;
		}
		if (currentClose.hit() != null)
			hit(currentClose);
		shots(shots() + 1);
		m_AllowShot = false;
		m_Timer.start();
		m_ProgressBarAnim.start();
	}

	/**
	 * @return m_Shots
	 */
	public int shots() {
		return m_Shots;
	}

	/**
	 * @param m_Shots m_Shots to set
	 */
	public void shots(int m_Shots) {
		this.m_Shots = m_Shots;
	}

	/**
	 * @return m_Hits
	 */
	public int numHits() {
		return m_Hits.size();
	}

	/**
	 * @param m_Hits m_Hits to set
	 */
	public void hit(RayHit hit) {
		this.m_Hits.add(hit);
		hit.hit().<Target>component(Target.class).remove();
	}

	public List<RayHit> hits() {
		return m_Hits;
	}

}
