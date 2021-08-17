package engine.animation;

import org.joml.Vector2f;

import engine.components.renderable.RenderableComponent;
import engine.shaders.uniforms.Vec2Uniform;

public class Vec2Animation extends Animation {
	
	private String m_UniformName;
	
	private Vector2f m_From, m_To;

	public Vec2Animation(Animatable rc, float length, String name, Vector2f from, Vector2f to) {
		super(rc, length);
		m_UniformName = name;
		m_From = from;
		m_To = to;
	}

	@Override
	public void process(float factor, Animatable rc) {
		Vector2f val = new Vector2f()
				.set(m_To)
				.sub(m_From)
				.mul(factor)
				.add(m_From);
		rc.uniform(m_UniformName, val);
	}
	
	public Vec2Animation to(Vector2f to) {
		m_To = to;
		return this;
	}
	
	public Vec2Animation from(Vector2f from) {
		m_From = from;
		return this;
	}

}
