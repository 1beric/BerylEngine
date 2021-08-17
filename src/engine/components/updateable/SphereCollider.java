package engine.components.updateable;

import org.joml.Vector3f;

import engine.components.renderable.Collider;
import engine.entities.Entity;
import engine.models.RayHit;

public class SphereCollider extends Collider {

	public static RayHit cast(Vector3f origin, Vector3f direction, Vector3f center, float radius, Entity ent) {
		float a = direction.dot(direction);
		Vector3f originToCenter = origin.sub(center, new Vector3f());
		float b = 2 * direction.dot(originToCenter);
		float c = originToCenter.dot(originToCenter) - radius * radius;
		float d = b * b - 4 * a * c;
		if (d <= 0)
			return Collider.castDefault(origin, direction);
		float t = (float) ((-b + Math.sqrt(d)) / (2 * a));
		if (t < 0)
			t = (float) ((-b + Math.sqrt(d)) / (2 * a));
		Vector3f point = origin.add(direction.mul(t, new Vector3f()), new Vector3f());
		Vector3f normal = point.sub(center, new Vector3f()).normalize();
		return new RayHit(ent, t, point, origin, direction, normal);
	}

	private float m_Radius = 1;

	@Override
	public RayHit cast(Vector3f origin, Vector3f direction) {
		if (entity() == null || transform() == null)
			return Collider.castDefault(origin, direction);
		float r = transform().scale().x * m_Radius;
		Vector3f center = transform().position();
		float a = direction.dot(direction);
		Vector3f originToCenter = origin.sub(center, new Vector3f());
		float b = 2 * direction.dot(originToCenter);
		float c = originToCenter.dot(originToCenter) - r * r;
		float d = b * b - 4 * a * c;
		if (d <= 0)
			return super.cast(origin, direction);
		float t = (float) ((-b + Math.sqrt(d)) / (2 * a));
		if (t < 0)
			t = (float) ((-b + Math.sqrt(d)) / (2 * a));
		Vector3f oToP = direction.mul(t, new Vector3f());
		Vector3f point = origin.add(oToP, new Vector3f());
		Vector3f normal = point.sub(center, new Vector3f()).normalize();
		return new RayHit(entity(), oToP.length(), point, origin, direction, normal);
//		return super.cast(origin, direction);
	}

	public void radius(float radius) {
		m_Radius = radius;
	}

	public float radius() {
		return m_Radius;
	}

}
