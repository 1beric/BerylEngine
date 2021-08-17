package engine.events.window;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class WindowFocusEvent extends Event {
	
	private Window m_Window;
	
	public WindowFocusEvent(Window window) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Application.val());
		super.type(EventType.WindowFocus);
		
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
