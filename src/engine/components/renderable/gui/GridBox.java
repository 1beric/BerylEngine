package engine.components.renderable.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.events.Event;
import engine.models.RawModel;
import engine.models.Rect;
import engine.models.Texture;
import engine.shaders.uniforms.Uniform;
import engine.util.string.StringTools;

public class GridBox extends Mesh2 {

	private Vector2f m_HorizontalBounds, m_VerticalBounds;

	private List<LineBox> m_List;

	private Vector2f m_Padding;

	private boolean m_RowMajor;

	public GridBox(float left, float top) {
		m_HorizontalBounds = new Vector2f(left, left);
		m_VerticalBounds = new Vector2f(top, top);
		m_Padding = new Vector2f(0);
		m_List = new ArrayList<>();
		m_RowMajor = true;
	}

	public GridBox(float left, float top, boolean rowMajor) {
		m_HorizontalBounds = new Vector2f(left, left);
		m_VerticalBounds = new Vector2f(top, top);
		m_Padding = new Vector2f(0);
		m_List = new ArrayList<>();
		m_RowMajor = rowMajor;
	}

	@Override
	public void init() {
		for (Mesh2 m : m_List)
			m.init();
	}

	@Override
	public void update() {
		for (Mesh2 m : m_List)
			m.update();
	}

	@Override
	public void lateUpdate() {
		for (Mesh2 m : m_List)
			m.lateUpdate();
	}

	@Override
	public void destroy() {
		for (Mesh2 m : m_List)
			m.destroy();
	}

	private void recalculateBounds() {
		if (m_RowMajor) {
			float top = m_VerticalBounds.x;
			float right = 0;
			for (LineBox hb : m_List) {
				hb.left(m_HorizontalBounds.x);
				hb.top(top);
				hb.padding(m_Padding.x);
				hb.recalculateBounds();
				m_VerticalBounds.y = hb.verticalBounds().y;
				if (right < hb.horizontalBounds().y)
					right = hb.horizontalBounds().y;
				top = hb.verticalBounds().y + m_Padding.y;
			}
			m_HorizontalBounds.y = right;
		} else {
			float left = m_HorizontalBounds.x;
			float bottom = 0;
			for (LineBox vb : m_List) {
				vb.left(left);
				vb.top(m_VerticalBounds.x);
				vb.padding(m_Padding.y);
				vb.recalculateBounds();
				m_HorizontalBounds.y = vb.horizontalBounds().y;
				if (bottom < vb.verticalBounds().y)
					bottom = vb.verticalBounds().y;
				left = vb.horizontalBounds().y + m_Padding.x;
			}
			m_VerticalBounds.y = bottom;
		}
	}

	public GridBox add(int row, Mesh2 mesh) {
		if (m_RowMajor)
			while (m_List.size() <= row)
				m_List.add(new HBox(m_HorizontalBounds.x, m_VerticalBounds.x));
		else
			while (m_List.size() <= row)
				m_List.add(new VBox(m_HorizontalBounds.x, m_VerticalBounds.x));
		m_List.get(row).add(mesh);
		mesh.entity(entity());
		recalculateBounds();
		return this;
	}

	public GridBox add(int row, int col, Mesh2 mesh) {
		m_List.get(row).add(col, mesh);
		mesh.entity(entity());
		recalculateBounds();
		return this;
	}

	public GridBox addAll(int row, Collection<? extends Mesh2> meshes) {
		m_List.get(row).addAll(meshes);
		for (Mesh2 m : meshes)
			m.entity(entity());
		recalculateBounds();
		return this;
	}

	public GridBox addAll(int row, int col, Collection<? extends Mesh2> meshes) {
		m_List.get(row).addAll(col, meshes);
		for (Mesh2 m : meshes)
			m.entity(entity());
		recalculateBounds();
		return this;
	}

	public GridBox padding(Vector2f padding) {
		m_Padding = padding;
		recalculateBounds();
		return this;
	}

	@Override
	public List<Rect> allRects() {
		List<Rect> out = new ArrayList<>();
		for (Mesh2 m : m_List)
			if (m.enabled())
				out.addAll(m.allRects());
		return out;
	}

	@Override
	public Vector2f verticalBounds() {
		return m_VerticalBounds;
	}

	@Override
	public Vector2f horizontalBounds() {
		return m_HorizontalBounds;
	}

	@Override
	public void translate(Vector2f translation) {
		for (Mesh2 m : m_List)
			m.translate(translation);
	}

	@Override
	public Mesh2 clone() {
		GridBox out = new GridBox(m_HorizontalBounds.x, m_VerticalBounds.x);
		out.padding(m_Padding);
		int i = 0;
		for (LineBox hb : m_List) {
			for (Mesh2 m : hb.list())
				out.add(i, m.clone());
		}
		return out;
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return null;
	}

	@Override
	public boolean handleAllEvents(Event event) {
		for (Mesh2 m : m_List)
			if (m.handleEvent(event))
				return true;
		return false;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "GridBox {", StringTools.indentl(indentAmt + 1),
				"horizontal bounds: ", horizontalBounds().toString(), StringTools.indentl(indentAmt + 1),
				"vertical bounds: ", verticalBounds().toString(), StringTools.indentl(indentAmt + 1),
				"padding: " + m_Padding, StringTools.indentl(indentAmt + 1), "elements {\n",
				StringTools.buildString(indentAmt + 2, m_List), StringTools.indentl(indentAmt + 1), "}",
				StringTools.indentl(indentAmt), "}");
	}

	@Override
	public Texture render(Object... args) {
		if (!enabled())
			return null;
		Vector2f resolution = (Vector2f) args[0];
		RawModel model = (RawModel) args[1];
		for (Mesh2 m : m_List)
			m.render(resolution, model);
		return null;
	}

}
