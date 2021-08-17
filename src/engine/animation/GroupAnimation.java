package engine.animation;

import java.util.ArrayList;
import java.util.List;

public class GroupAnimation extends Animation {

	private List<Animation> m_Animations;

	public GroupAnimation() {
		super(null, 0);
		m_Animations = new ArrayList<>();
	}

	public GroupAnimation addAnimation(Animation anim) {
		m_Animations.add(anim);
		return this;
	}

	@Override
	public void process(float factor, Animatable animatable) {
	}

	@Override
	public Animation play() {
		for (Animation anim : m_Animations)
			anim.play();
		return this;
	}

	@Override
	public Animation start() {
		for (Animation anim : m_Animations)
			anim.start();
		return this;
	}

	@Override
	public Animation pause() {
		for (Animation anim : m_Animations)
			anim.pause();
		return this;
	}

	@Override
	public Animation reversed(boolean r) {
		for (Animation anim : m_Animations)
			anim.reversed(r);
		return this;
	}

}
