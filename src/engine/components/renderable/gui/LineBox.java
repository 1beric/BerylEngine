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

public abstract class LineBox extends Mesh2 {

	protected List<Mesh2> m_List;

	protected float m_Padding;

	protected Vector2f m_VerticalBounds, m_HorizontalBounds;

	public LineBox(float left, float top) {
		m_List = new ArrayList<>();
		m_VerticalBounds = new Vector2f(top, top);
		m_HorizontalBounds = new Vector2f(left, left);
	}

	public abstract void recalculateBounds();

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

	public LineBox add(Mesh2 mesh) {
		m_List.add(mesh);
		mesh.entity(entity());
		recalculateBounds();
		return this;
	}

	public LineBox add(int index, Mesh2 mesh) {
		m_List.add(index, mesh);
		mesh.entity(entity());
		recalculateBounds();
		return this;
	}

	public LineBox addAll(Collection<? extends Mesh2> meshes) {
		m_List.addAll(meshes);
		for (Mesh2 m : meshes)
			m.entity(entity());
		recalculateBounds();
		return this;
	}

	public LineBox addAll(int index, Collection<? extends Mesh2> meshes) {
		m_List.addAll(index, meshes);
		for (Mesh2 m : meshes)
			m.entity(entity());
		recalculateBounds();
		return this;
	}

	public LineBox padding(float val) {
		m_Padding = val;
		return this;
	}

	public LineBox left(float left) {
		m_HorizontalBounds.x = left;
		recalculateBounds();
		return this;
	}

	public LineBox top(float top) {
		m_VerticalBounds.x = top;
		recalculateBounds();
		return this;
	}

	public List<Mesh2> list() {
		return m_List;
	}

	@Override
	public List<Rect> allRects() {
		List<Rect> out = new ArrayList<>();
		for (Mesh2 m : m_List)
			out.addAll(m.allRects());
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
	public Vector2f horizontalBounds() {
		return m_HorizontalBounds;
	}

	@Override
	public Vector2f verticalBounds() {
		return m_VerticalBounds;
	}

	@Override
	public void translate(Vector2f translation) {
		m_HorizontalBounds = horizontalBounds().add(new Vector2f(translation.x));
		m_VerticalBounds = verticalBounds().add(new Vector2f(translation.y));
		for (Mesh2 m : m_List)
			m.translate(translation);
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
