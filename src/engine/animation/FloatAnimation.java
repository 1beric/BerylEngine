package engine.animation;

public class FloatAnimation extends Animation {
	
	private String m_UniformName;
	
	private float m_From, m_To;

	public FloatAnimation(Animatable rc, float length, String name, float from, float to) {
		super(rc, length);
		m_UniformName = name;
		m_From = from;
		m_To = to;
	}

	@Override
	public void process(float factor, Animatable rc) {
		rc.uniform(m_UniformName, m_From + factor * (m_To - m_From));
	}

}
