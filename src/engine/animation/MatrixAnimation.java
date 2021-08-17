package engine.animation;


import org.joml.Matrix4f;

import engine.components.renderable.RenderableComponent;
import engine.shaders.uniforms.MatrixUniform;

public class MatrixAnimation extends Animation {
	
	private String m_UniformName;
	
	private Matrix4f m_From, m_To;

	public MatrixAnimation(Animatable rc, float length, String name, Matrix4f from, Matrix4f to) {
		super(rc, length);
		m_UniformName = name;
		m_From = from;
		m_To = to;
	}

	@Override
	public void process(float factor, Animatable rc) {
		Matrix4f val = new Matrix4f()
				.set(m_To)
				.sub(m_From)
				.scale(factor)
				.add(m_From);
		rc.uniform(m_UniformName, val);
	}

}
