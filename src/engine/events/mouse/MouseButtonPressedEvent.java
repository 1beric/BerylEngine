package engine.events.mouse;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class MouseButtonPressedEvent extends Event {

	private Window m_Window;
	private int m_Button;
	private int m_RepeatCount;
	
	public MouseButtonPressedEvent(Window window, int button, int repeatCount, int mods) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Mouse.or(EventCategory.Input));
		super.type(EventType.MouseButtonPressed);
		
		m_Window = window;
		m_Button = button;
		m_RepeatCount = repeatCount;
		
		
		
	}
	
	public Window window() {
		return m_Window;
	}
	
	public int button() {
		return m_Button;
	}
	
	public int repeatCount() {
		return m_RepeatCount;
	}
	
	@Override
	public String toString() {
		return name() + ":\n\t" + m_Window + "\n\t" + m_Button + " (" + m_RepeatCount + ")";
	}

}
