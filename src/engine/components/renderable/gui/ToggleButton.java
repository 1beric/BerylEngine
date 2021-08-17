package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.util.lambdas.BooleanFn;

public class ToggleButton extends Button {

	private boolean m_Toggled;
	private BooleanFn m_Toggle;
	
	public ToggleButton(Vector2f horizontalBounds, Vector2f verticalBounds) {
		super(horizontalBounds, verticalBounds);
		super.release((obj,btn,pos)->{
			if (btn == 0) toggle();
		});
	}
	
	public ToggleButton(float width, float height) {
		super(width, height);
		super.release((obj,btn,pos)->{
			if (btn == 0) toggle();
		});
	}
	
	public ToggleButton toggle(BooleanFn fn) {
		m_Toggle = fn;
		return this;
	}
	
	
	public ToggleButton toggle() {
		m_Toggled = !m_Toggled;
		if (m_Toggle != null) m_Toggle.handle(this, m_Toggled);
		return this;
	}
	
	public ToggleButton toggled(boolean on) {
		m_Toggled = on;
		if (m_Toggle != null) m_Toggle.handle(this, m_Toggled);
		return this;
	}
	
	public boolean toggled() {
		return m_Toggled;
	}
	
	@Override
	public Mesh2 clone() {
		ToggleButton c = new ToggleButton(horizontalBounds(), verticalBounds());
		c.color(color());
		c.borderWidth(borderWidth());
		c.borderColor(borderColor());
		c.opacity(opacity());
		c.borderRadius(borderRadius());
		c.hover(m_Hover);
		c.unhover(m_Unhover);
		c.press(m_Press);
		c.release(m_Release);
		c.toggle(m_Toggle);
		c.toggled(m_Toggled);
		return c;
	}

}
