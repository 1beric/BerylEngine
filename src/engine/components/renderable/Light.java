package engine.components.renderable;

import engine.shaders.Shader;
import engine.util.Color;
import engine.util.string.StringTools;

public abstract class Light extends RenderableComponent {

	protected Color m_Color;
	
	public Color color() {
		return m_Color;
	}
	
	public abstract void load(Shader shader);
	
	public Light color(Color color) {
		m_Color = color;
		return this;
	}
	

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt), "Light {",
				StringTools.indentl(indentAmt + 1), m_Enabled ? "enabled" : "disabled",
				StringTools.indentl(indentAmt + 1), "color: ", m_Color.toString(),
				StringTools.indentl(indentAmt), "}"
		);
	}

	@Override
	public String toString() {
		return string(0);
	}

}
