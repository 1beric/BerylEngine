package engine.events;

import engine.events.keyboard.KeyPressedEvent;
import engine.events.keyboard.KeyReleasedEvent;
import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseButtonReleasedEvent;
import engine.events.mouse.MouseEnterWindowEvent;
import engine.events.mouse.MouseExitWindowEvent;
import engine.events.mouse.MouseMovedEvent;
import engine.events.mouse.MouseScrolledEvent;
import engine.events.window.WindowCloseEvent;
import engine.events.window.WindowFocusEvent;
import engine.events.window.WindowLostFocusEvent;
import engine.events.window.WindowResizeEvent;

public interface EventHandler {

	public default boolean handleEvent(Event event) {
		if (handleAllEvents(event))
			return true;
		switch (event.type()) {
		case KeyPressed:
			return handleKeyPressed((KeyPressedEvent) event);
		case KeyReleased:
			return handleKeyReleased((KeyReleasedEvent) event);
		case MouseButtonPressed:
			return handleMouseButtonPressed((MouseButtonPressedEvent) event);
		case MouseButtonReleased:
			return handleMouseButtonReleased((MouseButtonReleasedEvent) event);
		case MouseEnterWindow:
			return handleMouseEnterWindow((MouseEnterWindowEvent) event);
		case MouseExitWindow:
			return handleMouseExitWindow((MouseExitWindowEvent) event);
		case MouseMoved:
			return handleMouseMoved((MouseMovedEvent) event);
		case MouseScrolled:
			return handleMouseScrolled((MouseScrolledEvent) event);
		case WindowClose:
			return handleWindowClose((WindowCloseEvent) event);
		case WindowFocus:
			return handleWindowFocus((WindowFocusEvent) event);
		case WindowLostFocus:
			return handleWindowLostFocus((WindowLostFocusEvent) event);
		case WindowResize:
			return handleWindowResize((WindowResizeEvent) event);
		case None:
			return false;
		}
		return false;
	}

	public default boolean handleAllEvents(Event event) {
		return false;
	}

	public default boolean handleKeyPressed(KeyPressedEvent kpe) {
		return false;
	}

	public default boolean handleKeyReleased(KeyReleasedEvent kre) {
		return false;
	}

	public default boolean handleMouseButtonPressed(MouseButtonPressedEvent mbpe) {
		return false;
	}

	public default boolean handleMouseButtonReleased(MouseButtonReleasedEvent mbre) {
		return false;
	}

	public default boolean handleMouseEnterWindow(MouseEnterWindowEvent mewe) {
		return false;
	}

	public default boolean handleMouseExitWindow(MouseExitWindowEvent mewe) {
		return false;
	}

	public default boolean handleMouseMoved(MouseMovedEvent mme) {
		return false;
	}

	public default boolean handleMouseScrolled(MouseScrolledEvent mse) {
		return false;
	}

	public default boolean handleWindowClose(WindowCloseEvent wce) {
		return false;
	}

	public default boolean handleWindowFocus(WindowFocusEvent wfe) {
		return false;
	}

	public default boolean handleWindowLostFocus(WindowLostFocusEvent wlfe) {
		return false;
	}

	public default boolean handleWindowResize(WindowResizeEvent wre) {
		return false;
	}

}
