package engine.util.input;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.Application;
import engine.Window;
import engine.events.Event;
import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseButtonReleasedEvent;
import engine.events.mouse.MouseEnterWindowEvent;
import engine.events.mouse.MouseExitWindowEvent;
import engine.events.mouse.MouseMovedEvent;
import engine.events.mouse.MouseScrolledEvent;

public class MousePicker {

	private static int s_UpdateFrameDelay = 50;

	private static Window s_HoveredWindow;
	private static Vector2f s_PreviousPosition;
	private static Vector2f s_Position;

	private static Vector2f s_PreviousScroll;
	private static Vector2f s_Scroll;

	private static Set<Integer> s_ActiveButtons;

	private static boolean s_CursorShowing;

	public static void init(Window window) {

		s_HoveredWindow = window;

		s_ActiveButtons = new HashSet<>();

		double[] xpos = new double[1];
		double[] ypos = new double[1];

		GLFW.glfwGetCursorPos(Application.instance().primaryWindow().window(), xpos, ypos);

		s_PreviousPosition = new Vector2f((float) xpos[0], (float) ypos[0]);
		s_Position = new Vector2f((float) xpos[0], (float) ypos[0]);
		s_PreviousScroll = new Vector2f(0);
		s_Scroll = new Vector2f(0);

	}

	public static void update() {
		if (s_UpdateFrameDelay > 0) {
			s_UpdateFrameDelay--;
			return;
		}
		s_PreviousPosition = s_Position;
		s_PreviousScroll = s_Scroll;
	}

	public static void handleEvent(Event event) {

		if (event instanceof MouseButtonPressedEvent) {
			MouseButtonPressedEvent mbpe = (MouseButtonPressedEvent) event;
			s_ActiveButtons.add(mbpe.button());
		}

		if (event instanceof MouseButtonReleasedEvent) {
			MouseButtonReleasedEvent mbpe = (MouseButtonReleasedEvent) event;
			s_ActiveButtons.remove(mbpe.button());
		}

		if (event instanceof MouseMovedEvent) {
			MouseMovedEvent mme = (MouseMovedEvent) event;
			s_Position = new Vector2f(mme.x(), mme.y());
			if (s_UpdateFrameDelay > 0)
				s_PreviousPosition = s_Position;
		}

		if (event instanceof MouseEnterWindowEvent) {
			MouseEnterWindowEvent mewe = (MouseEnterWindowEvent) event;
			s_HoveredWindow = mewe.window();
		}

		if (event instanceof MouseExitWindowEvent) {

		}

		if (event instanceof MouseScrolledEvent) {
			MouseScrolledEvent mse = (MouseScrolledEvent) event;
			s_Scroll = new Vector2f(mse.x(), mse.y());
			if (s_UpdateFrameDelay > 0)
				s_PreviousScroll = s_Scroll;
		}

	}

	public static Vector2f deltaScroll() {
		return new Vector2f().set(s_Scroll).sub(s_PreviousScroll);
	}

	public static Vector2f deltaPosition() {
		return new Vector2f().set(s_Position).sub(s_PreviousPosition);
	}

	public static Vector2f position() {
		return new Vector2f().set(s_Position);
	}

	public static Vector2f scroll() {
		return new Vector2f().set(s_Scroll);
	}

	public static Window window() {
		return s_HoveredWindow;
	}

	public static boolean button(int b) {
		return s_ActiveButtons.contains(b);
	}

	public static void cursor(boolean show) {
		s_CursorShowing = show;
		GLFW.glfwSetInputMode(window().window(), GLFW.GLFW_CURSOR,
				show ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED);
	}

	public static boolean cursor() {
		return s_CursorShowing;
	}

}
