package engine.components.renderable.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.events.mouse.MouseMovedEvent;
import engine.models.Rect;
import engine.shaders.uniforms.Uniform;
import engine.util.lambdas.FloatFn;
import engine.util.lambdas.Vec2Fn;
import engine.util.string.StringTools;

public class ProgressBar extends Mesh2 {

	private Rectangle m_Background, m_ProgressRect;
	private float m_Progress;

	private FloatFn m_ValueUpdated;

	private boolean m_Hovering;
	private Vec2Fn m_Hover, m_Unhover;

	public ProgressBar(Vector2f horizontalBounds, Vector2f verticalBounds) {
		m_Background = new Rectangle(horizontalBounds, verticalBounds);
		m_ProgressRect = new Rectangle(new Vector2f(0), verticalBounds);
		m_Hovering = false;
	}

	public ProgressBar(float width, float height) {
		m_Background = new Rectangle(width, height);
		m_ProgressRect = new Rectangle(0, height);
		m_Hovering = false;
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
		out.add(m_Background.rect());
		out.add(m_ProgressRect.rect());
		return out;
	}

	public boolean contains(float x, float y) {
		return x >= horizontalBounds().x() && x <= horizontalBounds().y() && y >= verticalBounds().x()
				&& y <= verticalBounds().y();
	}

	public Rectangle background() {
		return m_Background;
	}

	public Rectangle progressRect() {
		return m_ProgressRect;
	}

	public float value() {
		return m_Progress;
	}

	public ProgressBar value(float val) {
		m_Progress = val;
		float left = horizontalBounds().x;
		m_ProgressRect.horizontalBounds(new Vector2f(left, left + val * (horizontalBounds().y - left)));
		if (m_ValueUpdated != null)
			m_ValueUpdated.handle(this, val);
		return this;
	}

	public ProgressBar valueUpdated(FloatFn fn) {
		m_ValueUpdated = fn;
		return this;
	}

	public ProgressBar hover(Vec2Fn fn) {
		m_Hover = fn;
		return this;
	}

	public ProgressBar unhover(Vec2Fn fn) {
		m_Unhover = fn;
		return this;
	}

	public boolean hovering() {
		return m_Hovering;
	}

	public void translate(Vector2f translation) {
		m_Background.translate(translation);
		m_ProgressRect.translate(translation);
	}

	public ProgressBar horizontalBounds(Vector2f bounds) {
		m_Background.horizontalBounds(bounds);
		value(m_Progress);
		return this;
	}

	public Vector2f horizontalBounds() {
		return m_Background.horizontalBounds();
	}

	public ProgressBar verticalBounds(Vector2f bounds) {
		m_Background.verticalBounds(bounds);
		m_ProgressRect.verticalBounds(bounds);
		return this;
	}

	public Vector2f verticalBounds() {
		return m_Background.verticalBounds();
	}

	/**
	 * gets the bar uniform
	 */
	@Override
	public <T extends Uniform> T uniform(String name) {
		return m_Background.<T>uniform(name);
	}

	@Override
	public Mesh2 clone() {
		ProgressBar out = new ProgressBar(horizontalBounds(), verticalBounds());
		out.m_Background = (Rectangle) m_Background.clone();
		out.m_ProgressRect = (Rectangle) m_ProgressRect.clone();
		out.value(value());
		out.hover(m_Hover);
		out.unhover(m_Unhover);
		out.valueUpdated(m_ValueUpdated);
		return out;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "ProgressBar {",
				StringTools.indentl(indentAmt + 1), "horizontal bounds: ", horizontalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "vertical bounds: ", verticalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "progress: ", "" + m_Progress, StringTools.indentl(indentAmt + 1),
				"hover {\n", StringTools.buildString(indentAmt + 2, m_Hover), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "unhover {\n", StringTools.buildString(indentAmt + 2, m_Unhover),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt + 1), "valueUpdated {\n",
				StringTools.buildString(indentAmt + 2, m_ValueUpdated), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "background {\n", m_Background.string(indentAmt + 2),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt + 1), "progress rectangle {\n",
				m_ProgressRect.string(indentAmt + 2), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt), "}");
	}

}
