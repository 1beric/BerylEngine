package engine.components.renderable.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import engine.Material;
import engine.components.renderable.Mesh2;
import engine.events.mouse.MouseMovedEvent;
import engine.models.Rect;
import engine.models.Texture;
import engine.shaders.uniforms.ColorUniform;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.Sampler2DUniform;
import engine.shaders.uniforms.Uniform;
import engine.util.Color;
import engine.util.lambdas.Vec2Fn;
import engine.util.string.StringTools;

public class Rectangle extends Mesh2 {

	private Rect m_Rect;
	private boolean m_Hovering;
	protected Vec2Fn m_Hover, m_Unhover;

	public Rectangle(Vector2f horizontalBounds, Vector2f verticalBounds) {
		m_Rect = new Rect(horizontalBounds, verticalBounds, Material.material("default2D"));
		m_Rect.horizontalBounds(horizontalBounds);
		m_Rect.verticalBounds(verticalBounds);
		m_Hovering = false;
	}

	public Rectangle(float width, float height) {
		m_Rect = new Rect(new Vector2f(0, width), new Vector2f(0, height), Material.material("default2D"));
		m_Rect.horizontalBounds(new Vector2f(0, width));
		m_Rect.verticalBounds(new Vector2f(0, height));
		m_Hovering = false;
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void lateUpdate() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public boolean handleMouseMoved(MouseMovedEvent mme) {
		boolean in = contains(mme.x(), mme.y());
		if (in && !m_Hovering) {
			m_Hovering = true;
			if (m_Hover != null)
				m_Hover.handle(this, new Vector2f(mme.x(), mme.y()));
			return true;
		} else if (in)
			return true;
		else if (!in && m_Hovering) {
			m_Hovering = false;
			if (m_Unhover != null)
				m_Unhover.handle(this, new Vector2f(mme.x(), mme.y()));
			return false;
		}
		return false;
	}

	@Override
	public List<Rect> allRects() {
		List<Rect> out = new ArrayList<Rect>();
		out.add(m_Rect);
		return out;
	}

	public boolean contains(float x, float y) {
		return x >= m_Rect.horizontalBounds().x() && x <= m_Rect.horizontalBounds().y()
				&& y >= m_Rect.verticalBounds().x() && y <= m_Rect.verticalBounds().y();
	}

	public Rectangle hover(Vec2Fn fn) {
		m_Hover = fn;
		return this;
	}

	public Rectangle unhover(Vec2Fn fn) {
		m_Unhover = fn;
		return this;
	}

	public boolean hovering() {
		return m_Hovering;
	}

	public Rect rect() {
		return m_Rect;
	}

	public void translate(Vector2f translation) {
		m_Rect.translate(translation);
	}

	public Rectangle borderWidth(float bw) {
		m_Rect.material().<FloatUniform>uniform("borderWidth").val(bw);
		return this;
	}

	public float borderWidth() {
		return m_Rect.material().<FloatUniform>uniform("borderWidth").val();
	}

	public Rectangle borderColor(Color color) {
		m_Rect.material().<ColorUniform>uniform("borderColor").val(color);
		return this;
	}

	public Color borderColor() {
		return m_Rect.material().<ColorUniform>uniform("borderColor").val();
	}

	public Rectangle borderRadius(float br) {
		m_Rect.material().<FloatUniform>uniform("borderRadius").val(br);
		return this;
	}

	public float borderRadius() {
		return m_Rect.material().<FloatUniform>uniform("borderRadius").val();
	}

	public Rectangle color(Color color) {
		m_Rect.material().<ColorUniform>uniform("color").val(color);
		return this;
	}

	public Color color() {
		return m_Rect.material().<ColorUniform>uniform("color").val();
	}

	public Rectangle image(Texture image) {
		m_Rect.material().<Sampler2DUniform>uniform("image").val(image);
		return this;
	}

	public Texture image() {
		return m_Rect.material().<Sampler2DUniform>uniform("image").val();
	}

	public Rectangle opacity(float op) {
		m_Rect.material().<FloatUniform>uniform("opacity").val(op);
		return this;
	}

	public float opacity() {
		return m_Rect.material().<FloatUniform>uniform("opacity").val();
	}

	public Rectangle horizontalBounds(Vector2f bounds) {
		m_Rect.horizontalBounds(bounds);
		return this;
	}

	public Vector2f horizontalBounds() {
		return new Vector2f().set(m_Rect.horizontalBounds());
	}

	public Rectangle verticalBounds(Vector2f bounds) {
		m_Rect.verticalBounds(bounds);
		return this;
	}

	public Vector2f verticalBounds() {
		return new Vector2f().set(m_Rect.verticalBounds());
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return m_Rect.material().<T>uniform(name);
	}

	@Override
	public Mesh2 clone() {
		Rectangle c = new Rectangle(horizontalBounds(), verticalBounds());
		c.color(color());
		c.image(image());
		c.borderWidth(borderWidth());
		c.borderColor(borderColor());
		c.opacity(opacity());
		c.borderRadius(borderRadius());
		c.hover(m_Hover);
		c.unhover(m_Unhover);
		return c;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Rectangle {", StringTools.indentl(indentAmt + 1),
				"horizontal bounds: ", horizontalBounds().toString(), StringTools.indentl(indentAmt + 1),
				"vertical bounds: ", verticalBounds().toString(), StringTools.indentl(indentAmt + 1), "hover {\n",
				StringTools.buildString(indentAmt + 2, m_Hover), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "unhover {\n", StringTools.buildString(indentAmt + 2, m_Unhover),
				StringTools.indentl(indentAmt + 1), "}\n", rect().string(indentAmt + 1), StringTools.indentl(indentAmt),
				"}");
	}

}
