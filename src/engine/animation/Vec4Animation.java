package engine.animation;

import org.joml.Vector4f;

import engine.components.renderable.RenderableComponent;
import engine.shaders.uniforms.Vec4Uniform;

public class Vec4Animation extends Animation {
	
	private String m_UniformName;
	
	private Vector4f m_From, m_To;

	public Vec4Animation(Animatable rc, float length, String name, Vector4f from, Vector4f to) {
		super(rc, length);
		m_UniformName = name;
		m_From = from;
		m_To = to;
	}

	@Override
	public void process(float factor, Animatable rc) {
		Vector4f val = new Vector4f()
				.set(m_To)
				.sub(m_From)
				.mul(factor)
				.add(m_From);
		rc.uniform(m_UniformName, val);
	}

}
