package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseMovedEvent;
import engine.events.window.WindowResizeEvent;

public class FullscreenRectangle extends Rectangle {

	public FullscreenRectangle(Vector2f screenResolution) {
		super(new Vector2f(0, screenResolution.x), new Vector2f(0, screenResolution.y));
	}

	@Override
	public boolean handleMouseButtonPressed(MouseButtonPressedEvent mbpe) {
		return true;
	}

	@Override
	public boolean handleMouseMoved(MouseMovedEvent mme) {
		return true;
	}

	@Override
	public boolean handleWindowResize(WindowResizeEvent wre) {
		horizontalBounds(new Vector2f(0, wre.width()));
		verticalBounds(new Vector2f(0, wre.height()));
		return false;
	}

}
