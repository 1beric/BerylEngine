package engine.events.mouse;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class MouseScrolledEvent extends Event {

	private Window m_Window;
	private float m_Dx, m_Dy;
	
	public MouseScrolledEvent(Window window, float dx, float dy) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Mouse.or(EventCategory.Input));
		super.type(EventType.MouseScrolled);
		
		m_Window = window;
		m_Dx = dx;
		m_Dy = dy;
	}
	
	public Window window() {
		return m_Window;
	}
	
	public float x() {
		return m_Dx;
	}
	
	public float y() {
		return m_Dy;
	}
	
	@Override
	public String toString() {
		return name() + ":\n\t" + m_Window + "\n\t(" + m_Dx + ", " + m_Dy + ")";
	}

}
