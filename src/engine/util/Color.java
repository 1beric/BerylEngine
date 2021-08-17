package engine.util;

import java.util.Random;

import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Color implements Stringable {

	private static final Color BLACK = new Color(0, 0, 0);
	private static final Color RED = new Color(1, 0, 0);
	private static final Color GREEN = new Color(0, 1, 0);
	private static final Color BLUE = new Color(0, 0, 1);
	private static final Color MAGENTA = new Color(1, 0, 1);
	private static final Color YELLOW = new Color(1, 1, 0);
	private static final Color CYAN = new Color(0, 1, 1);
	private static final Color WHITE = new Color(1, 1, 1);

	private static final Random r = new Random();

	public static Color parse(String str) {
		if (str.startsWith("palette")) {
			String[] split = str.split("\\s*\\$\\s*");
			return Palette.palette(split[1]).color(Integer.parseInt(split[2]));
		} else if (str.startsWith("#") || str.toLowerCase().startsWith("0x") || !str.contains(",")) {
			if (str.charAt(0) == '#')
				str = str.substring(1);
			if (str.toLowerCase().startsWith("0x"))
				str = str.substring(2);
			String r = str.substring(0, 2);
			String g = str.substring(2, 4);
			String b = str.substring(4, 6);
			return new Color(Integer.parseInt(r, 16) / 255f, Integer.parseInt(g, 16) / 255f,
					Integer.parseInt(b, 16) / 255f);
		} else {
			String[] split = str.split("\\s*,\\s*");
			return new Color(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
		}
	}

	public static Color black() {
		return new Color(BLACK);
	}

	public static Color red() {
		return new Color(RED);
	}

	public static Color green() {
		return new Color(GREEN);
	}

	public static Color blue() {
		return new Color(BLUE);
	}

	public static Color magenta() {
		return new Color(MAGENTA);
	}

	public static Color yellow() {
		return new Color(YELLOW);
	}

	public static Color cyan() {
		return new Color(CYAN);
	}

	public static Color white() {
		return new Color(WHITE);
	}

	public static Color rand() {
		return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
	}

	private float m_R, m_G, m_B;

	public Color() {
		init(0, 0, 0);
	}

	public Color(Color c) {
		init(c.r(), c.g(), c.b());
	}

	public Color(float val) {
		init(val, val, val);
	}

	public Color(float r, float g, float b) {
		init(r, g, b);
	}

	public Color(String hex) {

	}

	private void init(float r, float g, float b) {
		this.m_R = r;
		this.m_G = g;
		this.m_B = b;
	}

	public Color add(Color other) {
		return new Color(this.m_R + other.m_R, this.m_G + other.m_G, this.m_B + other.m_B);
	}

	public Color sub(Color other) {
		return new Color(this.m_R - other.m_R, this.m_G - other.m_G, this.m_B - other.m_B);
	}

	public Color mul(Color other) {
		return new Color(this.m_R * other.m_R, this.m_G * other.m_G, this.m_B * other.m_B);
	}

	public Color div(Color other) {
		return new Color(this.m_R / other.m_R, this.m_G / other.m_G, this.m_B / other.m_B);
	}

	public Color add(float other) {
		return new Color(this.m_R + other, this.m_G + other, this.m_B + other);
	}

	public Color sub(float other) {
		return new Color(this.m_R - other, this.m_G - other, this.m_B - other);
	}

	public Color mul(float other) {
		return new Color(this.m_R * other, this.m_G * other, this.m_B * other);
	}

	public Color div(float other) {
		return new Color(this.m_R / other, this.m_G / other, this.m_B / other);
	}

	public Color interpolate(float factor, Color other) {
		return new Color(m_R + factor * (other.m_R - m_R), m_G + factor * (other.m_G - m_G),
				m_B + factor * (other.m_B - m_B));
	}

	public Color clamp() {
		return new Color(java.lang.Math.min(java.lang.Math.max(this.m_R, 0), 1),
				java.lang.Math.min(java.lang.Math.max(this.m_G, 0), 1),
				java.lang.Math.min(java.lang.Math.max(this.m_B, 0), 1));
	}

	public float r() {
		return m_R;
	}

	public Color r(float r) {
		m_R = r;
		return this;
	}

	public float g() {
		return m_G;
	}

	public Color g(float g) {
		m_G = g;
		return this;
	}

	public float b() {
		return m_B;
	}

	public Color b(float b) {
		m_B = b;
		return this;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "(" + m_R + ", " + m_G + ", " + m_B + ")");
	}

	@Override
	public String toString() {
		return string(0);
	}

}
