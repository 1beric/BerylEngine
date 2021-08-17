package engine.events.window;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class WindowCloseEvent extends Event {
	
	private Window m_Window;
	
	public WindowCloseEvent(Window window) {
		super();
		super.categoryFlags(EventCategory.Application.val());
		super.name(this.getClass().getSimpleName());
		super.type(EventType.WindowClose);
		
		m_Window = window;
	}
	
	public Window window() {
		return m_Window;
	}
	
}
