package engine.util;

import org.lwjgl.glfw.GLFW;

public class Time {


	private static double m_Time, m_PreviousTime;
	
	public static void init() {
		m_Time = 0.0001f;
		m_PreviousTime = 0;
	}

	public static void update() {
		m_PreviousTime = m_Time;
		m_Time = GLFW.glfwGetTime();
	}
	
	
	
	
	
	
	
	
	
	public static float time() {
		return (float) m_Time;
	}
	
	public static float delta() {
		float d = (float) (m_Time - m_PreviousTime);
		return d == 0 ? 0.0001f : d;
	}
	
	
	
	
	

}
