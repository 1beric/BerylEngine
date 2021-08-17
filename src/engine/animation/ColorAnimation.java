package engine.animation;

import engine.util.Color;

public class ColorAnimation extends Animation {
	
	private String m_UniformName;
	
	private Color m_From, m_To;

	public ColorAnimation(Animatable rc, float length, String name, Color from, Color to) {
		super(rc, length);
		m_UniformName = name;
		m_From = from;
		m_To = to;
	}

	@Override
	public void process(float factor, Animatable rc) {
		rc.uniform(m_UniformName, m_From.interpolate(factor, m_To));
	}

}
