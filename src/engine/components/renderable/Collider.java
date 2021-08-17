package engine.components.renderable;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.components.Component;
import engine.entities.Entity;
import engine.events.Event;
import engine.models.RayHit;

public abstract class Collider extends RenderableComponent {

	public Collider() {
		
	}

	public static RayHit castDefault(Vector3f origin, Vector3f direction) {
		return new RayHit(null, Float.POSITIVE_INFINITY, null, origin, direction, null);
	}
	
	public RayHit cast(Vector3f origin, Vector3f direction) {
		return Collider.castDefault(origin, direction);
	}
	
}
