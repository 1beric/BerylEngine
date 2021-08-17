package engine.animation;

import org.joml.Vector3f;

import engine.components.renderable.RenderableComponent;
import engine.shaders.uniforms.Vec3Uniform;

public class Vec3Animation extends Animation {
	
	private String m_UniformName;
	
	private Vector3f m_From, m_To;

	public Vec3Animation(Animatable rc, float length, String name, Vector3f from, Vector3f to) {
		super(rc, length);
		m_UniformName = name;
		m_From = from;
		m_To = to;
	}

	@Override
	public void process(float factor, Animatable rc) {
		Vector3f val = new Vector3f()
				.set(m_To)
				.sub(m_From)
				.mul(factor)
				.add(m_From);
		rc.uniform(m_UniformName, val);
	}

}
