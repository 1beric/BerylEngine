package engine.events.mouse;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class MouseExitWindowEvent extends Event {

	private Window m_Window;
	
	public MouseExitWindowEvent(Window window) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Mouse.or(EventCategory.Input));
		super.type(EventType.MouseExitWindow);
		
		m_Window = window;
	}
	
	public Window window() {
		return m_Window;
	}
	
	@Override
	public String toString() {
		return name() + ":\n\t" + m_Window;
	}

}
