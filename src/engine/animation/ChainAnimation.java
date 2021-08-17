package engine.animation;

public class ChainAnimation extends Animation {

	private Animation[] m_Animations;

	public ChainAnimation(Animation... args) {
		super(null, 0);
		m_Animations = args;
		setupChain();
	}

	private void setupChain() {
		for (int i = 0; i < m_Animations.length - 1; i++) {
			int[] pos = new int[] { i + 1 };
			m_Animations[i].finished((obj, rev) -> m_Animations[pos[0]].start());
		}
//		m_Animations[m_Animations.length - 1].finished((obj, rev)->m_Animations[0].playFromStart());
	}

	@Override
	public void process(float factor, Animatable animatable) {
	}

	@Override
	public Animation play() {
		m_Animations[0].play();
		return this;
	}

	@Override
	public Animation start() {
		m_Animations[0].start();
		return this;
	}

	@Override
	public Animation pause() {
		for (Animation anim : m_Animations)
			anim.pause();
		return this;
	}

}
