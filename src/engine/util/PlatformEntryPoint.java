package engine.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class PlatformEntryPoint {


	public static void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Could not initialize GLFW!");
	}
	
	public static void destroyGLFW() {
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	public static void initOpenGL() {
		GL.createCapabilities();
	}
	public static void destroyGL() {
		GL.destroy();
	}
	
	
	public static void errorCheckGL() {
//		while (true) {
			int status = GL11.glGetError();
			switch (status) {
			case GL11.GL_NO_ERROR:
				return;
			default:
				System.err.printf("ERROR status (%d)\n", status);
				break;
			}
//		}
	}
	
	
	
}
