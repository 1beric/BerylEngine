package engine.animation;

import java.util.ArrayList;
import java.util.List;

import engine.Updateable;
import engine.util.Time;
import engine.util.lambdas.BooleanFn;

public abstract class Animation implements Updateable {

	private static List<Animation> s_Animations = new ArrayList<>();

	public static void updateAll() {
		for (Animation a : s_Animations)
			a.update();
	}

	private boolean m_Running, m_Reversed, m_Repeat;
	private BooleanFn m_Finished;

	private float m_Delay, m_DelayProgress;
	private float m_Length, m_LengthProgress;

	private Animatable m_Animatable;

	public Animation(Animatable animatable, float length) {
		m_Animatable = animatable;

		m_Length = length;
		m_LengthProgress = 0;
		m_Delay = 0;
		m_DelayProgress = 0;

		m_Repeat = false;
		m_Running = false;
		m_Reversed = false;

		m_Finished = (obj, r) -> {
		};

		s_Animations.add(this);
	}

	public Animation(float length) {
		this(null, length);
	}

	public abstract void process(float factor, Animatable animatable);

	@Override
	public void update() {
		if (!m_Running)
			return;
		if (m_DelayProgress < m_Delay) {
			m_DelayProgress += Time.delta();
			return;
		} else if (m_LengthProgress > m_Length && !m_Reversed) {
			if (m_Repeat) {
				m_LengthProgress = 0;
				m_DelayProgress = 0;
			} else
				finish(false);
			return;
		} else if (m_LengthProgress < 0 && m_Reversed) {
			if (m_Repeat) {
				m_LengthProgress = m_Length;
				m_DelayProgress = 0;
			} else
				finish(true);
			return;
		}

		process(m_LengthProgress / m_Length, m_Animatable);

		m_LengthProgress += Time.delta() * (m_Reversed ? -1 : 1);
	}

	public Animation play() {
		m_Running = true;
		return this;
	}

	public Animation start() {
		m_Running = true;
		if (m_Reversed)
			m_LengthProgress = m_Length;
		else
			m_LengthProgress = 0;
		m_DelayProgress = 0;
		return this;
	}

	public Animation pause() {
		m_Running = false;
		return this;
	}

	public Animation reversed(boolean rev) {
		m_Reversed = rev;
		return this;
	}

	public Animation repeat(boolean rep) {
		m_Repeat = rep;
		return this;
	}

	public Animation delay(float delay) {
		m_Delay = delay;
		return this;
	}

	private void finish(boolean reversed) {
		m_Running = false;
		process(reversed ? 0 : 1, m_Animatable);
		m_Finished.handle(this, reversed);
	}

	public Animation finished(BooleanFn fn) {
		m_Finished = fn;
		return this;
	}

	public BooleanFn finished() {
		return m_Finished;
	}

	public boolean running() {
		return m_Running;
	}

	public float progress() {
		return m_LengthProgress + m_DelayProgress;
	}

	public Animation length(float length) {
		m_Length = length;
		return this;
	}

	public float length() {
		return m_Length;
	}

	@Override
	public void init() {
	}

	@Override
	public void lateUpdate() {
	}

	@Override
	public void destroy() {
	}

	public static void destroyAll() {
		for (Animation anim : s_Animations)
			anim.destroy();
		s_Animations.clear();
	}

}
