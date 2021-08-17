package engine.components.renderable.gui;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.util.string.StringTools;

public class VBox extends LineBox {
	
	public VBox(float left, float top) {
		super(left, top);
	}

	@Override
	public void recalculateBounds() {
		float top = m_VerticalBounds.x;
		float bottom = top;
		float right = m_HorizontalBounds.x;
		for (Mesh2 mesh : m_List) {
			Vector2f translation = new Vector2f(m_HorizontalBounds.x - mesh.horizontalBounds().x, top - mesh.verticalBounds().x);
			mesh.translate(translation);
			bottom = mesh.verticalBounds().y;
			if (right < mesh.horizontalBounds().y)
				right = mesh.horizontalBounds().y;		
			top += (mesh.verticalBounds().y - mesh.verticalBounds().x) + m_Padding;
		}
		m_HorizontalBounds.y = right;
		m_VerticalBounds.y = bottom;
	}
	
	
	@Override
	public Mesh2 clone() {
		VBox out = new VBox(m_HorizontalBounds.x, m_VerticalBounds.x);
		out.padding(m_Padding);
		for (Mesh2 m : m_List)
			out.add(m.clone());
		return out;
	}
	
	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt), "VBox {",
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
