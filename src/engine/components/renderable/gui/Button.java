package engine.components.renderable.gui;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.events.mouse.MouseButtonPressedEvent;
import engine.events.mouse.MouseButtonReleasedEvent;
import engine.util.input.MousePicker;
import engine.util.lambdas.IntFn;
import engine.util.string.StringTools;

public class Button extends Rectangle {

	private Set<Integer> m_ButtonsDown;
	protected IntFn m_Press, m_Release;

	public Button(Vector2f horizontalBounds, Vector2f verticalBounds) {
		super(horizontalBounds, verticalBounds);
		m_ButtonsDown = new HashSet<>();
	}

	public Button(float width, float height) {
		super(width, height);
		m_ButtonsDown = new HashSet<>();
	}

	@Override
	public boolean handleMouseButtonPressed(MouseButtonPressedEvent mbpe) {
		if (hovering() && mbpe.repeatCount() == 0) {
			if (m_Press != null)
				m_Press.handle(this, mbpe.button(), MousePicker.position());
			m_ButtonsDown.add(mbpe.button());
			return true;
		} else if (hovering())
			return true;
		return false;
	}

	@Override
	public boolean handleMouseButtonReleased(MouseButtonReleasedEvent mbre) {
		if (m_ButtonsDown.contains(mbre.button())) {
			if (m_Release != null)
				m_Release.handle(this, mbre.button(), MousePicker.position());
			m_ButtonsDown.remove(mbre.button());
			return true;
		}
		return false;
	}

	public Button press(IntFn fn) {
		m_Press = fn;
		return this;
	}

	public Button release(IntFn fn) {
		m_Release = fn;
		return this;
	}

	@Override
	public Mesh2 clone() {
		Button c = new Button(horizontalBounds(), verticalBounds());
		c.color(color());
		c.borderWidth(borderWidth());
		c.borderColor(borderColor());
		c.opacity(opacity());
		c.borderRadius(borderRadius());
		c.hover(m_Hover);
		c.unhover(m_Unhover);
		c.press(m_Press);
		c.release(m_Release);
		return c;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Button {", StringTools.indentl(indentAmt + 1),
				"horizontal bounds: ", horizontalBounds().toString(), StringTools.indentl(indentAmt + 1),
				"vertical bounds: ", verticalBounds().toString(), StringTools.indentl(indentAmt + 1), "hover {\n",
				StringTools.buildString(indentAmt + 2, m_Hover), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "unhover {\n", StringTools.buildString(indentAmt + 2, m_Unhover),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt + 1), "press {\n",
				StringTools.buildString(indentAmt + 2, m_Press), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt + 1), "release {\n", StringTools.buildString(indentAmt + 2, m_Release),
				StringTools.indentl(indentAmt + 1), "}\n", rect().string(indentAmt + 1), StringTools.indentl(indentAmt),
				"}");
	}

}
