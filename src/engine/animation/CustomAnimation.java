package engine.animation;

import engine.util.lambdas.FloatFn;

public class CustomAnimation extends Animation {

	private FloatFn m_Fn;

	public CustomAnimation(float length, FloatFn fn) {
		super(length);
		m_Fn = fn;
	}

	@Override
	public void process(float factor, Animatable animatable) {
		m_Fn.handle(animatable, factor);
	}

}
