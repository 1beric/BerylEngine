package engine.components.updateable;

import engine.components.renderable.Camera;
import engine.components.renderable.Collider;
import engine.components.renderable.Transform;
import engine.entities.Entity;
import engine.events.mouse.MouseButtonPressedEvent;
import engine.models.RayHit;
import engine.util.input.MousePicker;

public class EditorPicker extends UpdateableComponent {

	@Override
	public void update() {

	}

	private Entity pickEntity() {

		Transform cam = entity().layer().<Camera>component(Camera.class).transform();
		RayHit currentClose = Collider.castDefault(cam.position(), cam.forward());

		for (Entity ent : entity().layer().entities()) {
			RayHit current = SphereCollider.cast(cam.position(), cam.forward(), ent.transform().position(), .2f, ent);
			if (current.hit() != null && current.hit().name().equals(cam.entity().name()))
				continue;
			if (current.distance() < currentClose.distance())
				currentClose = current;
		}
//		System.out.println(currentClose.hit());

		return currentClose != null ? currentClose.hit() : null;
	}

	@Override
	public boolean handleMouseButtonPressed(MouseButtonPressedEvent mbpe) {
		if (mbpe.button() == 0 && MousePicker.deltaPosition().length() == 0)
			entity().layer().select(pickEntity());
		return false;
	}

}
