package gameplay.targetPractice;

import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.Application;
import engine.Layer;
import engine.animation.Animatable;
import engine.animation.Animation;
import engine.animation.Timer;
import engine.components.renderable.gui.ProgressBar;
import engine.components.updateable.UpdateableComponent;
import engine.entities.Entity;
import engine.events.keyboard.KeyPressedEvent;
import engine.util.Color;
import engine.util.MathTools;

public class TargetSpawner extends UpdateableComponent {

	private Collection<Target> m_Targets;

	private Vector3f m_LowerBounds, m_UpperBounds;
	private Vector2f m_ScaleBounds;

	private Timer m_Timer;
	private float m_DeltaSeconds = 1f;
	private float m_InitialDelaySeconds = 5f;
	private float m_TargetLife = 2;

	private int m_TotalTargets = 0;

	@Override
	public void init() {
		m_Targets = new ArrayList<>();

		m_LowerBounds = new Vector3f(-5, -5, -8);
		m_UpperBounds = new Vector3f(5, 5, -2);
		m_ScaleBounds = new Vector2f(.2f, 1);

		m_Timer = new Timer(m_DeltaSeconds);
		m_Timer.finished((animatable, reversed) -> {
			spawnTarget();
			m_Timer.start();
		});

		ProgressBar pb = new ProgressBar(1200, 200);
		pb.background().color(new Color("#867873"));
		pb.progressRect().color(new Color("#867873").mul(1.5f));
		pb.translate(new Vector2f(500, 400));

		Animation pbAnim = new Animation(pb, m_InitialDelaySeconds) {
			@Override
			public void process(float factor, Animatable animatable) {
				pb.value(factor);
			}
		};
		pbAnim.finished((animatable, reversed) -> {
			m_Timer.start();
			Layer l = Application.instance().layer("hud");
			l.removeEntity(l.entity("gun progress"));
		});
		pbAnim.start();

		Application.instance().layer("hud").addEntity(new Entity("gun progress").addComponent(pb));
	}

	private void spawnTarget() {
		Target t = new Target(this, m_TargetLife);
		Entity tEnt = new Entity("Target " + m_TotalTargets++).addComponent(t);

		tEnt.transform().translate(MathTools.randomVec3(m_LowerBounds, m_UpperBounds));
		tEnt.transform().scale(new Vector3f(MathTools.rFloat(m_ScaleBounds)));

		entity().layer().addEntity(tEnt);

		m_Targets.add(t);
	}

	public Collection<Target> targets() {
		return m_Targets;
	}

	public void removeTarget(Target t) {
		m_Targets.remove(t);
	}

	public int totalTargets() {
		return m_TotalTargets;
	}

	public float targetLife() {
		return m_TargetLife;
	}

	@Override
	public boolean handleKeyPressed(KeyPressedEvent kpe) {
		if (kpe.key() == GLFW.GLFW_KEY_ESCAPE) {
			if (m_Timer.running()) {
				m_Timer.pause();
				m_Targets.forEach(target -> {
					if (target.timer() != null)
						target.timer().pause();
				});
			} else {
				m_Timer.play();
				m_Targets.forEach(target -> {
					if (target.timer() != null)
						target.timer().play();
				});
			}
		}
		return false;

	}

}
