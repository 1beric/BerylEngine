package engine.components.renderable;

import org.joml.Vector3f;

import engine.components.Component;
import engine.entities.Entity;
import engine.shaders.Shader;
import engine.shaders.uniforms.BooleanUniform;
import engine.shaders.uniforms.ColorUniform;
import engine.shaders.uniforms.Vec3Uniform;
import engine.util.Color;
import engine.util.string.StringTools;

public class PointLight extends Light {

	private Transform m_Transform;
	private Vector3f m_Attenuation;

	public PointLight() {
		m_Color = Color.white();
		m_Attenuation = new Vector3f(1, 0, 0);
	}

	@Override
	public Component entity(Entity entity) {
		super.entity(entity);
		m_Transform = entity.<Transform>component(Transform.class);
		if (m_Transform == null) {
			m_Transform = new Transform();
			entity.addComponent(m_Transform);
		}
		return this;
	}

	public Vector3f position() {
		return m_Transform.position();
	}

	public Vector3f attenuation() {
		return m_Attenuation;
	}

	@Override
	public void load(Shader shader) {
		shader.<Vec3Uniform>uniform("light").val(position());
		shader.<Vec3Uniform>uniform("attenuation").val(attenuation());
		shader.<ColorUniform>uniform("lightColor").val(color());
		shader.<BooleanUniform>uniform("pointLight").val(true);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "PointLight {",
				StringTools.indentl(indentAmt + 1), m_Enabled ? "enabled" : "disabled",
				StringTools.indentl(indentAmt + 1), "color: ", m_Color.toString(), StringTools.indentl(indentAmt + 1),
				"attenuation: ", m_Attenuation.toString(), StringTools.indentl(indentAmt), "}");
	}

	@Override
	public String toString() {
		return string(0);
	}

}
