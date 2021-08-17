package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.util.MathTools;
import engine.util.input.MousePicker;
import engine.util.string.StringTools;

public class HSlider extends Slider {

	private static float HOFF = 0.48f;
	private static float VOFF = 0.4f;
	private static float MIN_CURSOR = 1;
	
	public HSlider(Vector2f horizontalBounds, Vector2f verticalBounds, Vector2f valueBounds) {
		super(horizontalBounds, verticalBounds, valueBounds);
		
		float horizontalLength = horizontalBounds.y - horizontalBounds.x;
		float verticalLength = verticalBounds.y - verticalBounds.x;
		bar(new Rectangle(new Vector2f().set(horizontalBounds), new Vector2f(verticalBounds.x + verticalLength * VOFF, verticalBounds.y - verticalLength * VOFF)));
		cursor(new Rectangle(new Vector2f(horizontalBounds.x + horizontalLength * HOFF, horizontalBounds.y - horizontalLength * HOFF), new Vector2f().set(verticalBounds)));
	}
	
	public HSlider(float width, float height, Vector2f valueBounds) {
		super(width, height, valueBounds);
		
		float horizontalLength = horizontalBounds().y - horizontalBounds().x;
		float verticalLength = verticalBounds().y - verticalBounds().x;
		bar(new Rectangle(new Vector2f().set(horizontalBounds()), new Vector2f(verticalBounds().x + verticalLength * VOFF, verticalBounds().y - verticalLength * VOFF)));
		cursor(new Rectangle(new Vector2f(horizontalBounds().x + horizontalLength * HOFF, horizontalBounds().y - horizontalLength * HOFF), new Vector2f().set(verticalBounds())));
	}

	@Override
	public void update() {
		if (m_TrackingMouse) {
			float offset = Math.max((.5f - HOFF) * (m_SliderHorizontalBounds.y - m_SliderHorizontalBounds.x), MIN_CURSOR / 2f);
			Vector2f centerHB = new Vector2f(m_SliderHorizontalBounds.x + offset, m_SliderHorizontalBounds.y - offset);
			float newCursorCenter = MathTools.clamp(MousePicker.position().x, centerHB.x, centerHB.y);
			m_Progress = (newCursorCenter - centerHB.x) / (centerHB.y - centerHB.x);
			if (m_SnapAmount > 0) {
				m_Progress -= (m_Progress+m_SnapAmount/2f) % m_SnapAmount;
				m_Progress += m_SnapAmount/2f;
				m_Progress = Math.round(m_Progress / m_SnapAmount) * m_SnapAmount;
				newCursorCenter = centerHB.x + m_Progress * (centerHB.y - centerHB.x);
			}
			newCursorCenter = MathTools.clamp(newCursorCenter, centerHB.x, centerHB.y);
			Vector2f newCursorHB = new Vector2f(newCursorCenter - offset, newCursorCenter + offset);
			m_Cursor.horizontalBounds(newCursorHB);
			if (m_UpdateOnHold) m_ValueUpdated.handle(this, currentValue());
		}
	}
	
	@Override
	public Mesh2 clone() {
		Slider c = new HSlider(horizontalBounds(), verticalBounds(), m_ValueBounds);
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
				StringTools.indent(indentAmt), "HSlider {",
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
