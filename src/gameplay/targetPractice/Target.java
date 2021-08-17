package gameplay.targetPractice;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.animation.Timer;
import engine.components.renderable.Collider;
import engine.components.renderable.Mesh3;
import engine.components.updateable.SphereCollider;
import engine.components.updateable.UpdateableComponent;
import engine.events.keyboard.KeyPressedEvent;
import engine.models.RawModel;
import engine.models.RayHit;
import engine.models.creation.SphereBuilder;
import engine.util.Color;

public class Target extends UpdateableComponent {

	private static RawModel s_Sphere;

	private TargetSpawner m_Spawner;
	private Collider m_Collider;
	private Mesh3 m_Mesh;
	private Timer m_Timer;

	private float m_Lifetime;

	public Target(TargetSpawner spawner, float lifetime) {
		m_Spawner = spawner;
		m_Lifetime = lifetime;
	}

	@Override
	public void init() {

		if (s_Sphere == null)
			s_Sphere = SphereBuilder.build(50);

		m_Collider = new SphereCollider();
//		if (m_Collider instanceof SphereCollider) {
//			SphereCollider col = (SphereCollider) m_Collider;
//			col.radius(transform().scale().x);
//		}
		entity().addComponent(m_Collider);

		m_Mesh = new Mesh3(s_Sphere);
		m_Mesh.uniform("color", Color.rand());
		m_Mesh.uniform("reflectivity", .3f);
		m_Mesh.uniform("shineDamper", 1.6f);
		entity().addComponent(m_Mesh);

		m_Timer = new Timer(m_Lifetime);
		m_Timer.finished((animatable, reversed) -> this.remove());
		m_Timer.start();

	}

	public RayHit collides(Vector3f origin, Vector3f direction) {
		if (m_Removed)
			return Collider.castDefault(origin, direction);
		return m_Collider.cast(origin, direction);
	}

	@Override
	public boolean handleKeyPressed(KeyPressedEvent kpe) {
		if (kpe.key() == GLFW.GLFW_KEY_SPACE)
			m_Mesh.uniform("color", Color.rand());
		return false;
	}

	/**
	 * @return m_Mesh
	 */
	public Mesh3 mesh() {
		return m_Mesh;
	}

	/**
	 * @param m_Mesh m_Mesh to set
	 */
	public void mesh(Mesh3 mesh) {
		this.m_Mesh = mesh;
	}

	private boolean m_Removed;

	public void remove() {
		if (m_Removed)
			return;
		m_Spawner.removeTarget(this);
		entity().layer().removeEntity(entity());
		m_Timer.pause();
		m_Removed = true;
	}

	public float timeAlive() {
		return m_Timer.progress();
	}

	public Timer timer() {
		return m_Timer;
	}

}
