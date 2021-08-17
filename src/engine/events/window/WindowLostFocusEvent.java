package engine.events.window;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class WindowLostFocusEvent extends Event {
	
	private Window m_Window;
	
	public WindowLostFocusEvent(Window window) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Application.val());
		super.type(EventType.WindowLostFocus);
		
		m_Window = window;
	}
	
	public Window window() {
		return m_Window;
	}
	
	@Override
	public String toString() {
		return name() + ": " + m_Window;
	}

}
