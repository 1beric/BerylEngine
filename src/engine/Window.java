package engine;

import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import engine.events.Event;
import engine.events.keyboard.KeyPressedEvent;
import engine.events.keyboard.KeyReleasedEvent;
import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseButtonReleasedEvent;
import engine.events.mouse.MouseEnterWindowEvent;
import engine.events.mouse.MouseExitWindowEvent;
import engine.events.mouse.MouseMovedEvent;
import engine.events.mouse.MouseScrolledEvent;
import engine.events.window.WindowCloseEvent;
import engine.events.window.WindowFocusEvent;
import engine.events.window.WindowLostFocusEvent;
import engine.events.window.WindowResizeEvent;
import engine.util.PlatformEntryPoint;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Window implements Updateable, Stringable {

	public static final int DEFAULT_WIDTH  = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	public static final String DEFAULT_TITLE = "Game!";
	public static final int DEFAULT_FPS_CAP = 60;
	
	private static boolean glInitialized = false;
	
	private static long initialized = MemoryUtil.NULL;
	
	public static Window createWindow() {
		return createWindow(DEFAULT_TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public static Window createWindow(int width, int height) {
		return createWindow(DEFAULT_TITLE, width, height);
	}

	public static Window createWindow(String title) {
		return createWindow(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public static Window createWindow(String title, int width, int height) {
		
		// Configure GLFW
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
		
		// Create the window
		long window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, initialized);
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		return new Window(window, width, height);
	}
	
	
	
	
	private long m_Window;
	private int m_Width, m_Height;
	private boolean m_Focused;
	
	public Window(long window, int width, int height) {
		m_Window = window;
		m_Width = width;
		m_Height = height;
		m_Focused = false;

		this.callbacks()
			.center()
			.windowContext()
			.vsync(true)
			.show(true);
		
		if (!glInitialized) {
			PlatformEntryPoint.initOpenGL();
			glInitialized = true;
			initialized = m_Window;
		}
	}
	
	@Override
	public void init() {}
	
	@Override
	public void update() {
		if (GLFW.glfwWindowShouldClose(m_Window)) {
			Application.instance().handleEvent(new WindowCloseEvent(this));
			return;
		}
		GLFW.glfwSwapBuffers(m_Window);
	}	
	
	@Override
	public void lateUpdate() { }
	
	public void close() {
		GLFW.glfwSetWindowShouldClose(m_Window, true);
	}
	
	@Override
	public void destroy() {
		Callbacks.glfwFreeCallbacks(m_Window);
		GLFW.glfwDestroyWindow(m_Window);
	}
	
	
	/**
	 * Show or hide the window
	 * @param show Whether to show or hide the window
	 * @return this
	 */
	public Window show(boolean show) {
		if (show) GLFW.glfwShowWindow(m_Window);
		else	  GLFW.glfwHideWindow(m_Window);
		return this;
	}
	
	/**
	 * Turn vsync on or off for this window
	 * @param on Whether vsync should be on or off
	 * @return this
	 */
	public Window vsync(boolean on) {
		GLFW.glfwSwapInterval(on ? 1 : 0);
		return this;
	}
	
	/**
	 * Center this window on it's display
	 * @return this
	 */
	public Window center() {
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(
			m_Window,
			(vidmode.width() - m_Width) / 2,
			(vidmode.height() - m_Height) / 2);
		return this;
	}
	
	/**
	 * Make the OpenGL context current
	 * @return this
	 */
	public Window windowContext() {
		GLFW.glfwMakeContextCurrent(m_Window);
		return this;
	}
	
	public long window() {
		return m_Window;
	}
	
	public Window width(int width) {
		m_Width = width;
		return this;
	}
	
	public int width() {
		return m_Width;
	}

	public Window height(int height) {
		m_Height = height;
		return this;
	}
	
	public int height() {
		return m_Height;
	}
	
	public Vector2f resolution() {
		return new Vector2f(m_Width, m_Height);
	}
	
	public Window callbacks() {
		// WindowCloseEvent
		GLFW.glfwSetWindowCloseCallback(m_Window, _window->this.close());
		
		// WindowResizeEvent
		GLFW.glfwSetWindowSizeCallback(m_Window, (_window, w, h)->{
			Application.instance().handleEvent(new WindowResizeEvent(this, w, h));
		});

		// WindowFocusEvent and WindowLostFocusEvent
		GLFW.glfwSetWindowFocusCallback(m_Window, (_window, focused)->{
			Application.instance().handleEvent(focused ? new WindowFocusEvent(this) : new WindowLostFocusEvent(this));
		});
		
		// MouseEnterWindowEvent and MouseExitWindowEvent
		GLFW.glfwSetCursorEnterCallback(m_Window, (_window, entered)->{
			Application.instance().handleEvent(entered ? new MouseEnterWindowEvent(this) : new MouseExitWindowEvent(this));
		});
		
		// MouseButtonPressedEvent and MouseButtonReleasedEvent
		GLFW.glfwSetMouseButtonCallback(m_Window, (_window, button, action, mods)->{
			Event e = null;
			switch (action) {
			case GLFW.GLFW_PRESS:
				e = new MouseButtonPressedEvent(this, button, 0, mods);
				break;
			case GLFW.GLFW_REPEAT:
				e = new MouseButtonPressedEvent(this, button, 1, mods);
				break;
			case GLFW.GLFW_RELEASE:
				e = new MouseButtonReleasedEvent(this, button, mods);
				break;
			}
			Application.instance().handleEvent(e);
		});
		
		// MouseMovedEvent
		GLFW.glfwSetCursorPosCallback(m_Window, (_window, x, y)->{
			Application.instance().handleEvent(new MouseMovedEvent(this, (int)x, (int)y));
		});
		
		// MouseScrolledEvent
		GLFW.glfwSetScrollCallback(m_Window, (_window, dx, dy)->{
			Application.instance().handleEvent(new MouseScrolledEvent(this, (float)dx, (float)dy));
		});
		
		// KeyPressedEvent and KeyReleasedEvent
		GLFW.glfwSetKeyCallback(m_Window, (_window, key, scancode, action, mods)->{
			Event e = null;
			switch (action) {
			case GLFW.GLFW_PRESS:
				e = new KeyPressedEvent(this, key, scancode, 0, mods);
				break;
			case GLFW.GLFW_REPEAT:
				e = new KeyPressedEvent(this, key, scancode, 1, mods);
				break;
			case GLFW.GLFW_RELEASE:
				e = new KeyReleasedEvent(this, key, scancode, mods);
				break;
			}
			Application.instance().handleEvent(e);
		});
		
		return this;
	}
	
	@Override
	public String toString() {
		return string(0);
	}
	
	public void focus(boolean focused) {
		m_Focused = focused;
	}
	
	public boolean focused() {
		return m_Focused;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt), "Window: ", ""+m_Window,
				", (", ""+m_Width, ":", ""+m_Height, ")",
				m_Focused ? ", focused" : ""
		);
	}

}
