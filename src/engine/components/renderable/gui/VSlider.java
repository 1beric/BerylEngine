package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.util.MathTools;
import engine.util.input.MousePicker;
import engine.util.string.StringTools;

public class VSlider extends Slider {

	private static float HOFF = 0.4f;
	private static float VOFF = 0.48f;
	private static float MIN_CURSOR = 1;
	private static float MIN_BAR = 1;
	
	public VSlider(Vector2f horizontalBounds, Vector2f verticalBounds, Vector2f valueBounds) {
		super(horizontalBounds, verticalBounds, valueBounds);
		float horizontalLength = horizontalBounds().y - horizontalBounds().x;
		float verticalLength = verticalBounds().y - verticalBounds().x;
		bar(new Rectangle(new Vector2f(horizontalBounds().x + horizontalLength * HOFF, horizontalBounds().y - horizontalLength * HOFF), new Vector2f().set(verticalBounds())));
		cursor(new Rectangle(new Vector2f().set(horizontalBounds()), new Vector2f(verticalBounds().x + verticalLength * VOFF, verticalBounds().y - verticalLength * VOFF)));
	}

	public VSlider(float width, float height, Vector2f valueBounds) {
		super(width, height, valueBounds);
		float horizontalLength = horizontalBounds().y - horizontalBounds().x;
		float verticalLength = verticalBounds().y - verticalBounds().x;
		bar(new Rectangle(new Vector2f(horizontalBounds().x + horizontalLength * HOFF, horizontalBounds().y - horizontalLength * HOFF), new Vector2f().set(verticalBounds())));
		cursor(new Rectangle(new Vector2f().set(horizontalBounds()), new Vector2f(verticalBounds().x + verticalLength * VOFF, verticalBounds().y - verticalLength * VOFF)));
	}

	
	@Override
	public void update() {
		if (m_TrackingMouse) {
			float offset = Math.max((.5f - VOFF) * (m_SliderVerticalBounds.y - m_SliderVerticalBounds.x), MIN_CURSOR / 2f);
			Vector2f centerVB = new Vector2f(m_SliderVerticalBounds.x + offset, m_SliderVerticalBounds.y - offset);
			float newCursorCenter = MathTools.clamp(MousePicker.position().y, centerVB.x, centerVB.y);
			m_Progress = (newCursorCenter - centerVB.x) / (centerVB.y - centerVB.x);
			if (m_SnapAmount > 0) {
				m_Progress -= (m_Progress+m_SnapAmount/2f) % m_SnapAmount;
				m_Progress += m_SnapAmount/2f;
				m_Progress = Math.round(m_Progress / m_SnapAmount) * m_SnapAmount;
				newCursorCenter = centerVB.x + m_Progress * (centerVB.y - centerVB.x);
			}
			newCursorCenter = MathTools.clamp(newCursorCenter, centerVB.x, centerVB.y);
			Vector2f newCursorVB = new Vector2f(newCursorCenter - offset, newCursorCenter + offset);
			m_Cursor.verticalBounds(newCursorVB);
			m_Progress = 1 - m_Progress;
			if (m_UpdateOnHold) m_ValueUpdated.handle(this, currentValue());
		}
	}
	
	@Override
	public Mesh2 clone() {
		Slider c = new VSlider(horizontalBounds(), verticalBounds(), m_ValueBounds);
		c.m_Bar = (Rectangle) m_Bar.clone();
		c.m_Cursor = (Rectangle) m_Cursor.clone();
		c.hover(m_Hover);
		c.unhover(m_Unhover);
		c.updateOnHold(m_UpdateOnHold);
		c.snapAmount(m_SnapAmount);
		c.valueUpdated(m_ValueUpdated);
		c.m_Progress = m_Progress;
		return c;
	}
	
	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt), "VSlider {",
				StringTools.indentl(indentAmt + 1), "horizontal bounds: ", horizontalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "vertical bounds: ", verticalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "value: ", ""+currentValue(),
				StringTools.indentl(indentAmt + 1), "value bounds: ", m_ValueBounds.toString(),
				StringTools.indentl(indentAmt + 1), "updates on hold: ", ""+m_UpdateOnHold,
				StringTools.indentl(indentAmt + 1), "snap amount: ", ""+m_SnapAmount,
				StringTools.indentl(indentAmt + 1), "hover {\n",
				StringTools.buildString(indentAmt + 2, m_Hover),
				StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "unhover {\n",
				StringTools.buildString(indentAmt + 2, m_Unhover),
				StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "valueUpdated {\n",
				StringTools.buildString(indentAmt + 2, m_ValueUpdated),
				StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "bar {\n",
				m_Bar.string(indentAmt + 2),
				StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "cursor {\n",
				m_Cursor.string(indentAmt + 2),
				StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt), "}"
		);
	}
	
}
