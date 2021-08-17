package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.util.string.StringTools;

public class HBox extends LineBox {
	
	public HBox(float left, float top) {
		super(left, top);
	}

	@Override
	public void recalculateBounds() {
		float left = m_HorizontalBounds.x;
		float right = left;
		float bottom = m_VerticalBounds.x;
		for (Mesh2 mesh : m_List) {
			Vector2f translation = new Vector2f(left - mesh.horizontalBounds().x, m_VerticalBounds.x - mesh.verticalBounds().x);
			mesh.translate(translation);
			right = mesh.horizontalBounds().y;
			if (bottom < mesh.verticalBounds().y)
				bottom = mesh.verticalBounds().y;		
			left += (mesh.horizontalBounds().y - mesh.horizontalBounds().x) + m_Padding;
		}
		m_VerticalBounds.y = bottom;
		m_HorizontalBounds.y = right;
	}
	
	
	@Override
	public Mesh2 clone() {
		HBox out = new HBox(m_HorizontalBounds.x, m_VerticalBounds.x);
		out.padding(m_Padding);
		for (Mesh2 m : m_List)
			out.add(m.clone());
		return out;
	}
	
	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt), "HBox {",
				StringTools.indentl(indentAmt + 1), "horizontal bounds: ", horizontalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "vertical bounds: ", verticalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "padding: "+m_Padding,
				StringTools.indentl(indentAmt + 1), "elements {\n",
				StringTools.buildString(indentAmt + 2, m_List),
				StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt), "}"
		);
	}

}
