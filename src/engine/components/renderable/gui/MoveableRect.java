package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseButtonReleasedEvent;
import engine.util.input.MousePicker;
import engine.util.string.StringTools;

public class MoveableRect extends Rectangle {

	private boolean m_ButtonDown;

	public MoveableRect(Vector2f horizontalBounds, Vector2f verticalBounds) {
		super(horizontalBounds, verticalBounds);
	}

	public MoveableRect(float width, float height) {
		super(width, height);
	}

	@Override
	public void update() {
		if (m_ButtonDown)
			rect().translate(MousePicker.deltaPosition());
	}

	@Override
	public boolean handleMouseButtonPressed(MouseButtonPressedEvent mbpe) {
		super.handleMouseButtonPressed(mbpe);
		if (hovering()) {
			if (mbpe.button() == 0)
				m_ButtonDown = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean handleMouseButtonReleased(MouseButtonReleasedEvent mbre) {
		super.handleMouseButtonReleased(mbre);
		if (m_ButtonDown && mbre.button() == 0) {
			m_ButtonDown = false;
			return true;
		}
		return false;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "MoveableRect {",
				StringTools.indentl(indentAmt + 1), "horizontal bounds: ", horizontalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "vertical bounds: ", verticalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "hover {\n", StringTools.buildString(indentAmt + 2, m_Hover),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt + 1), "unhover {\n",
				StringTools.buildString(indentAmt + 2, m_Unhover), StringTools.indentl(indentAmt + 1), "}\n",
				rect().string(indentAmt + 1), StringTools.indentl(indentAmt), "}");
	}

}
