package engine.events.window;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class WindowResizeEvent extends Event {
	
	private Window m_Window;
	private int m_Width, m_Height;
	
	public WindowResizeEvent(Window window, int width, int height) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Application.val());
		super.type(EventType.WindowResize);
		
		m_Window = window;
		m_Width = width;
		m_Height = height;
	}
	
	public Window window() {
		return m_Window;
	}
	
	public int width() {
		return m_Width;
	}
	
	public int height() {
		return m_Height;
	}
	
	public float aspectRatio() {
		return m_Width * (1.0f / m_Height);
	}
	
	@Override
	public String toString() {
		return name() + "(" + m_Window + "): (" + m_Width + ", " + m_Height + ")";
	}

}
