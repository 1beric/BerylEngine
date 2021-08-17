package engine.events.mouse;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class MouseButtonReleasedEvent extends Event {

	private Window m_Window;
	private int m_Button;
	
	public MouseButtonReleasedEvent(Window window, int button, int mods) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Mouse.or(EventCategory.Input));
		super.type(EventType.MouseButtonReleased);
		
		m_Window = window;
		m_Button = button;
		
		
	}
	
	public Window window() {
		return m_Window;
	}
	
	public int button() {
		return m_Button;
	}
	
	@Override
	public String toString() {
		return name() + ":\n\t" + m_Window + "\n\t" + m_Button;
	}

}
