package engine.components.renderable.gui;

import java.util.List;

import org.joml.Vector2f;

import engine.components.renderable.Mesh2;
import engine.events.Event;
import engine.models.Rect;
import engine.models.Texture;
import engine.shaders.uniforms.Uniform;
import engine.util.Color;

public class ColorDisplay extends Mesh2 {

	private LineBox m_List;

	public ColorDisplay(float left, float top, float elemWidth, float elemHeight, float padding, Color display,
			Color bar, Color cursor) {
		m_List = new VBox(left, top);
		m_List.padding(padding);

		Slider lightR = new HSlider(elemWidth, elemHeight, new Vector2f(0, 1));
		lightR.updateOnHold(true);
		lightR.valueUpdated((_obj, val) -> display.r(val));
		lightR.bar().color(bar);
		lightR.cursor().color(cursor);
		lightR.cursor().borderRadius(10);
		m_List.add(lightR);

		Slider lightG = new HSlider(elemWidth, elemHeight, new Vector2f(0, 1));
		lightG.updateOnHold(true);
		lightG.valueUpdated((_obj, val) -> display.g(val));
		lightG.bar().color(bar);
		lightG.cursor().color(cursor);
		lightG.cursor().borderRadius(10);
		m_List.add(lightG);

		Slider lightB = new HSlider(elemWidth, elemHeight, new Vector2f(0, 1));
		lightB.updateOnHold(true);
		lightB.valueUpdated((_obj, val) -> display.b(val));
		lightB.bar().color(bar);
		lightB.cursor().color(cursor);
		lightB.cursor().borderRadius(10);
		m_List.add(lightB);

	}

	@Override
	public void init() {
		m_List.init();
	}

	@Override
	public void update() {
		m_List.update();
	}

	@Override
	public void lateUpdate() {
		m_List.lateUpdate();
	}

	@Override
	public void destroy() {
		m_List.destroy();
	}

	@Override
	public boolean handleAllEvents(Event event) {
		return m_List.handleEvent(event);
	}

	@Override
	public List<Rect> allRects() {
		return m_List.allRects();
	}

	@Override
	public Texture render(Object... args) {
		return m_List.render(args);
	}

	@Override
	public Vector2f verticalBounds() {
		return m_List.verticalBounds();
	}

	@Override
	public Vector2f horizontalBounds() {
		return m_List.horizontalBounds();
	}

	@Override
	public void translate(Vector2f translation) {
		m_List.translate(translation);
	}

	@Override
	public Mesh2 clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return null;
	}

}
