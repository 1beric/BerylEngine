package engine.events.mouse;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class MouseEnterWindowEvent extends Event {

	private Window m_Window;
	
	public MouseEnterWindowEvent(Window window) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Mouse.or(EventCategory.Input));
		super.type(EventType.MouseEnterWindow);
		
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
