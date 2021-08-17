package engine.events.mouse;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class MouseMovedEvent extends Event {

	private Window m_Window;
	private int m_X, m_Y;
	
	public MouseMovedEvent(Window window, int x, int y) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Mouse.or(EventCategory.Input));
		super.type(EventType.MouseMoved);
		
		m_Window = window;
		m_X = x;
		m_Y = y;
	}
	
	public Window window() {
		return m_Window;
	}
	
	public int x() {
		return m_X;
	}
	
	public int y() {
		return m_Y;
	}
	
	@Override
	public String toString() {
		return name() + ":\n\t" + m_Window + "\n\t(" + m_X + ", " + m_Y + ")";
	}

}
