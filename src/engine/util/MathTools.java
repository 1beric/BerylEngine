package engine.util;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathTools {

	private static Random m_Random = new Random();

	public static Vector2f clamp(Vector2f v, Vector2f l, Vector2f u) {
		return new Vector2f(clamp(v.x, u.x, l.x), clamp(v.y, u.y, l.y));
	}

	public static float clamp(float v, float l, float u) {
		return Math.max(Math.min(v, u), l);
	}

	public static int rInt(int bound) {
		return m_Random.nextInt(bound) - bound / 2;
	}

	public static float rFloat() {
		return m_Random.nextFloat();
	}

	public static float rFloat(float a, float b) {
		return a + m_Random.nextFloat() * (b - a);
	}

	public static float rFloat(Vector2f bounds) {
		return rFloat(bounds.x, bounds.y);
	}

	public static Vector3f randomVec3() {
		return new Vector3f(rFloat(), rFloat(), rFloat());
	}

	public static Vector3f randomVec3(float a, float b) {
		return new Vector3f(rFloat(a, b), rFloat(a, b), rFloat(a, b));
	}

	public static Vector3f randomVec3(float xa, float xb, float ya, float yb, float za, float zb) {
		return new Vector3f(rFloat(xa, xb), rFloat(ya, yb), rFloat(za, zb));
	}

	public static Vector3f randomVec3(Vector2f bounds) {
		return randomVec3(bounds.x, bounds.y);
	}

	public static Vector3f randomVec3(Vector2f x, Vector2f y, Vector2f z) {
		return randomVec3(x.x, x.y, y.x, y.y, z.x, z.y);
	}

	public static Vector3f randomVec3(Vector3f lb, Vector3f ub) {
		return randomVec3(lb.x, ub.x, lb.y, ub.y, lb.z, ub.z);
	}

	public static Vector2f randomVec2() {
		return new Vector2f(rFloat(), rFloat());
	}

	public static Vector2f randomVec2(float a, float b) {
		return new Vector2f(rFloat(a, b), rFloat(a, b));
	}

	public static Vector2f randomVec2(float xa, float xb, float ya, float yb) {
		return new Vector2f(rFloat(xa, xb), rFloat(ya, yb));
	}

	public static Vector2f randomVec2(Vector2f x, Vector2f y) {
		return randomVec2(x.x, x.y, y.x, y.y);
	}

	public static Vector2f randomVec2bounds(Vector2f lb, Vector2f ub) {
		return randomVec2(lb.x, ub.x, lb.y, ub.y);
	}

}
