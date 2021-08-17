package engine.components.renderable;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.components.Component;
import engine.entities.Entity;
import engine.util.string.StringTools;

public class Camera extends RenderableComponent {

	private Transform m_Transform;

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

	public Vector3f rotation() {
		return m_Transform.rotation();
	}

	public Vector3f rotationRadians() {
		return m_Transform.rotationRadians();
	}

	public Vector3f position() {
		return m_Transform.position();
	}

	public Matrix4f viewMatrix() {
		Matrix4f viewMatrix = new Matrix4f().identity();
		viewMatrix = viewMatrix.rotate(rotationRadians().x, 1, 0, 0);
		viewMatrix = viewMatrix.rotate(rotationRadians().y, 0, 1, 0);
		viewMatrix = viewMatrix.rotate(rotationRadians().z, 0, 0, 1);
		viewMatrix = viewMatrix.translate(new Vector3f().set(position()).mul(-1));
		return viewMatrix;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Camera {", StringTools.indentl(indentAmt + 1),
				m_Enabled ? "enabled" : "disabled", StringTools.indentl(indentAmt), "}");
	}

	@Override
	public String toString() {
		return string(0);
	}

}
