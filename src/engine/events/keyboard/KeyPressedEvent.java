package engine.events.keyboard;

import org.lwjgl.glfw.GLFW;

import engine.Window;
import engine.events.Event;
import engine.events.EventCategory;
import engine.events.EventType;

public class KeyPressedEvent extends Event {

	private Window m_Window;
	private int m_Key;
	private int m_Scancode;
	private int m_RepeatCount;

	public KeyPressedEvent(Window window, int key, int scancode, int repeatCount, int mods) {
		super();
		super.name(this.getClass().getSimpleName());
		super.categoryFlags(EventCategory.Keyboard.or(EventCategory.Input));
		super.type(EventType.KeyPressed);

		m_Window = window;
		m_Key = key;
		m_Scancode = scancode;
		m_RepeatCount = repeatCount;

	}

	public Window window() {
		return m_Window;
	}

	public int key() {
		return m_Key;
	}

	public int repeatCount() {
		return m_RepeatCount;
	}

	public int scancode() {
		return m_Scancode;
	}

	@Override
	public String toString() {
		return name() + ":\n\t" + m_Window + "\n\t" + GLFW.glfwGetKeyName(m_Key, m_Scancode) + " (" + m_RepeatCount
				+ ")";
	}

}
