package engine.components.renderable.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseButtonReleasedEvent;
import engine.events.mouse.MouseMovedEvent;
import engine.models.RawModel;
import engine.models.Rect;
import engine.models.Texture;
import engine.shaders.uniforms.Uniform;
import engine.util.lambdas.FloatFn;
import engine.util.lambdas.Vec2Fn;
import engine.util.string.StringTools;

public abstract class Slider extends Mesh2 {

	protected Rectangle m_Bar, m_Cursor;
	protected Vector2f m_ValueBounds;
	protected float m_Progress;

	protected FloatFn m_ValueUpdated;

	protected Vector2f m_SliderHorizontalBounds, m_SliderVerticalBounds;

	protected boolean m_Hovering, m_TrackingMouse;
	protected Vec2Fn m_Hover, m_Unhover;

	protected boolean m_UpdateOnHold;
	protected float m_SnapAmount;

	public Slider(Vector2f horizontalBounds, Vector2f verticalBounds, Vector2f valueBounds) {
		m_SliderHorizontalBounds = horizontalBounds;
		m_SliderVerticalBounds = verticalBounds;
		m_ValueBounds = valueBounds;

		m_Hovering = false;
		m_TrackingMouse = false;
		m_UpdateOnHold = false;
		m_ValueUpdated = (obj, val) -> {
		};
	}

	public Slider(float width, float height, Vector2f valueBounds) {
		m_SliderHorizontalBounds = new Vector2f(0, width);
		m_SliderVerticalBounds = new Vector2f(0, height);
		m_ValueBounds = valueBounds;

		m_Hovering = false;
		m_TrackingMouse = false;
		m_UpdateOnHold = false;
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
	public boolean handleMouseButtonPressed(MouseButtonPressedEvent mbpe) {
		if (m_Hovering && mbpe.button() == 0 && mbpe.repeatCount() == 0) {
			mouseDown();
			return true;
		} else if (m_TrackingMouse || m_Hovering)
			return true;
		return false;
	}

	@Override
	public boolean handleMouseButtonReleased(MouseButtonReleasedEvent mbre) {
		if (m_TrackingMouse && mbre.button() == 0) {
			mouseUp();
			return true;
		}
		return false;
	}

	@Override
	public List<Rect> allRects() {
		List<Rect> out = new ArrayList<Rect>();
		out.add(m_Bar.rect());
		out.add(m_Cursor.rect());
		return out;
	}

	public boolean contains(float x, float y) {
		return x >= horizontalBounds().x() && x <= horizontalBounds().y() && y >= verticalBounds().x()
				&& y <= verticalBounds().y();
	}

	public Rectangle bar() {
		return m_Bar;
	}

	public Slider bar(Rectangle b) {
		m_Bar = b;
		return this;
	}

	public Rectangle cursor() {
		return m_Cursor;
	}

	public Slider cursor(Rectangle c) {
		m_Cursor = c;
		return this;
	}

	private Slider mouseDown() {
		if (m_TrackingMouse)
			return this;
		m_TrackingMouse = true;
		return this;
	}

	private Slider mouseUp() {
		if (!m_TrackingMouse)
			return this;
		m_TrackingMouse = false;
		if (m_ValueUpdated != null)
			m_ValueUpdated.handle(this, currentValue());
		return this;
	}

	public float currentValue() {
		return m_ValueBounds.x + m_Progress * (m_ValueBounds.y - m_ValueBounds.x);
	}

	public Slider valueUpdated(FloatFn fn) {
		m_ValueUpdated = fn;
		return this;
	}

	public Slider updateOnHold(boolean update) {
		m_UpdateOnHold = update;
		return this;
	}

	public Slider snapAmount(float amt) {
		m_SnapAmount = amt;
		return this;
	}

	public Slider hover(Vec2Fn fn) {
		m_Hover = fn;
		return this;
	}

	public Slider unhover(Vec2Fn fn) {
		m_Unhover = fn;
		return this;
	}

	public boolean hovering() {
		return m_Hovering;
	}

	public void translate(Vector2f translation) {
		m_SliderHorizontalBounds = horizontalBounds().add(new Vector2f(translation.x));
		m_SliderVerticalBounds = verticalBounds().add(new Vector2f(translation.y));
		m_Bar.translate(translation);
		m_Cursor.translate(translation);

	}

	public Slider horizontalBounds(Vector2f bounds) {
		m_SliderHorizontalBounds = bounds;
		return this;
	}

	public Vector2f horizontalBounds() {
		return m_SliderHorizontalBounds;
	}

	public Slider verticalBounds(Vector2f bounds) {
		m_SliderVerticalBounds = bounds;
		return this;
	}

	public Vector2f verticalBounds() {
		return m_SliderVerticalBounds;
	}

	/**
	 * gets the bar uniform
	 */
	@Override
	public <T extends Uniform> T uniform(String name) {
		return m_Bar.<T>uniform(name);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), getClass().getSimpleName(), " {",
				StringTools.indentl(indentAmt + 1), "horizontal bounds: ", horizontalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "vertical bounds: ", verticalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "value: ", "" + currentValue(), StringTools.indentl(indentAmt + 1),
				"value bounds: ", m_ValueBounds.toString(), StringTools.indentl(indentAmt + 1), "updates on hold: ",
				"" + m_UpdateOnHold, StringTools.indentl(indentAmt + 1), "snap amount: ", "" + m_SnapAmount,
				StringTools.indentl(indentAmt + 1), "hover {\n", StringTools.buildString(indentAmt + 2, m_Hover),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt + 1), "unhover {\n",
				StringTools.buildString(indentAmt + 2, m_Unhover), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "valueUpdated {\n",
				StringTools.buildString(indentAmt + 2, m_ValueUpdated), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "bar {\n", m_Bar.string(indentAmt + 2),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt + 1), "cursor {\n",
				m_Cursor.string(indentAmt + 2), StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt),
				"}");
	}

	@Override
	public Texture render(Object... args) {
		if (!enabled())
			return null;
		Vector2f resolution = (Vector2f) args[0];
		RawModel model = (RawModel) args[1];
		m_Bar.render(resolution, model);
		m_Cursor.render(resolution, model);
		return null;
	}

	@Override
	public Mesh2 clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
