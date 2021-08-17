package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import engine.components.Component;
import engine.components.renderable.Camera;
import engine.components.renderable.DirectionalLight;
import engine.components.renderable.PostProcessor;
import engine.components.updateable.EditorPicker;
import engine.entities.Entity;
import engine.events.Event;
import engine.events.EventHandler;
import engine.models.FrameBuffer;
import engine.models.FrameBuffer.DepthType;
import engine.models.Texture;
import engine.rendering.GuiRenderer;
import engine.rendering.ModelRenderer;
import engine.rendering.RenderType;
import engine.rendering.SkyboxRenderer;
import engine.util.Enableable;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Layer implements Updateable, Stringable, Enableable, EventHandler {

	private static Map<String, Layer> s_Layers = new HashMap<>();

	public static Layer layer(String name) {
		return s_Layers.get(name);
	}

	private String m_Name;

	private List<Entity> m_Entities, m_EntitiesReversed, m_EntitiesToRemove, m_EntitiesToAdd;
	private RenderType m_RenderType;
	private Window m_Window;

	private FrameBuffer m_MSFrameBuffer;
	private FrameBuffer m_FrameBuffer1;
	private FrameBuffer m_FrameBuffer2;

	private Entity m_Selected;

	private boolean m_Initialized;

	public Layer(String name, Window window, RenderType renderType) {
		s_Layers.put(name, this);
		m_Name = name;
		m_Entities = new ArrayList<>();
		m_EntitiesReversed = new ArrayList<>();
		m_EntitiesToAdd = new ArrayList<>();
		m_EntitiesToRemove = new ArrayList<>();
		m_Window = window;
		m_RenderType = renderType;
		m_Enabled = true;
		switch (m_RenderType) {
		case EDITOR:
			addEntity(new Entity("Editor Component").addComponent(new EditorPicker()));
		case MODEL:
			addEntity(new Entity("Main Camera").addComponent(new Camera()));
			addEntity(new Entity("Main Light").addComponent(new DirectionalLight()));
			m_MSFrameBuffer = new FrameBuffer(window().resolution(), DepthType.DEPTH_RENDER_BUFFER, true);
			m_FrameBuffer1 = new FrameBuffer(window().resolution(), DepthType.DEPTH_TEXTURE, false);
			m_FrameBuffer2 = new FrameBuffer(window().resolution(), DepthType.DEPTH_TEXTURE, false);
			break;
		case GUI:
			m_FrameBuffer1 = new FrameBuffer(window().resolution(), DepthType.DEPTH_TEXTURE, false);
			break;
		case NONE:
			break;
		}
	}

	public Texture render(Texture background) {
		if (!enabled())
			return background;
		GL11.glViewport(0, 0, window().width(), window().height());
		switch (m_RenderType) {
		case MODEL:
			return renderMODEL(background, false);
		case EDITOR:
			return renderMODEL(background, true);
		case GUI:
			return renderGUI(background);
		case NONE:
			return background;
		}
		return Texture.white();
	}

	private Texture renderGUI(Texture background) {
		m_FrameBuffer1.bind();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GuiRenderer.render(background);
		GuiRenderer.render(this);
		m_FrameBuffer1.unbind();
		PostProcessor pp = this.<PostProcessor>component(PostProcessor.class);
		if (pp != null)
			return pp.render(m_FrameBuffer1.texture1(), GuiRenderer.screen());
		else
			return m_FrameBuffer1.texture1();
	}

	private Texture renderMODEL(Texture background, boolean editor) {
		m_MSFrameBuffer.bind();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GuiRenderer.render(background);
		SkyboxRenderer.render(this);
		ModelRenderer.render(this);
		m_MSFrameBuffer.unbind();
		m_MSFrameBuffer.resolve(GL30.GL_COLOR_ATTACHMENT0, m_FrameBuffer1);
		m_MSFrameBuffer.resolve(GL30.GL_COLOR_ATTACHMENT1, m_FrameBuffer2);
		if (editor) {
			m_MSFrameBuffer.bind();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GuiRenderer.render(m_FrameBuffer1.texture1());
			ModelRenderer.renderTransforms(this);
			m_MSFrameBuffer.unbind();
			m_MSFrameBuffer.resolve(GL30.GL_COLOR_ATTACHMENT0, m_FrameBuffer1);
			m_MSFrameBuffer.resolve(GL30.GL_COLOR_ATTACHMENT1, m_FrameBuffer2);
		}
		PostProcessor pp = this.<PostProcessor>component(PostProcessor.class);
		if (pp != null)
			return pp.render(m_FrameBuffer1.texture1(), GuiRenderer.screen());
		else
			return m_FrameBuffer1.texture1();
	}

	@Override
	public void init() {
		handleAdds();
		handleRemoves();
		for (Entity entity : m_Entities)
			entity.init();
		m_Initialized = true;
	}

	@Override
	public void update() {
		if (!enabled())
			return;
		handleAdds();
		handleRemoves();
		for (Entity entity : m_Entities)
			entity.update();
	}

	private void handleAdds() {
		List<Entity> copy = new ArrayList<>(m_EntitiesToAdd);
		m_EntitiesToAdd.clear();
		for (Entity entity : copy) {
			m_Entities.add(entity);
			m_EntitiesReversed.add(0, entity);
			entity.layer(this);
			if (m_Initialized)
				entity.init();
		}
	}

	private void handleRemoves() {
		List<Entity> copy = new ArrayList<>(m_EntitiesToRemove);
		m_EntitiesToRemove.clear();
		for (Entity entity : copy) {
			m_Entities.remove(entity);
			m_EntitiesReversed.remove(entity);
			entity.destroy();
		}
	}

	@Override
	public void lateUpdate() {
		if (!enabled())
			return;
		for (Entity entity : m_Entities)
			entity.lateUpdate();
	}

	@Override
	public boolean handleAllEvents(Event event) {
		if (!enabled())
			return false;
		for (Entity entity : m_EntitiesReversed)
			if (entity.handleEvent(event))
				return true;
		return false;
	}

	@Override
	public void destroy() {
		for (Entity entity : m_Entities)
			entity.destroy();
	}

	public void addEntity(Entity entity) {
		m_EntitiesToAdd.add(entity);
	}

	public List<Entity> entities() {
		return m_Entities;
	}

	public Entity entity(String name) {
		for (Entity entity : m_Entities)
			if (entity.name().equals(name))
				return entity;
		for (Entity entity : m_EntitiesToAdd)
			if (entity.name().equals(name))
				return entity;
		return null;
	}

	public Window window() {
		return m_Window;
	}

	public boolean is3D() {
		return m_RenderType == RenderType.MODEL;
	}

	public boolean is2D() {
		return m_RenderType == RenderType.GUI;
	}

	public void removeEntity(String name) {
		removeEntity(entity(name));
	}

	public void removeEntity(Entity toRemove) {
		m_EntitiesToRemove.add(toRemove);
	}

	public void select(Entity e) {
		m_Selected = e;
	}

	public Entity selected() {
		return m_Selected;
	}

	public <T extends Component> T component(Class<?> c) {
		for (Entity entity : m_Entities) {
			T out = entity.<T>component(c);
			if (out != null)
				return out;
		}
		for (Entity entity : m_EntitiesToAdd) {
			T out = entity.<T>component(c);
			if (out != null)
				return out;
		}
		return null;
	}

	public <T extends Component> List<T> components(Class<?> c) {
		List<T> out = new ArrayList<>();
		for (Entity entity : m_Entities) {
			T component = entity.<T>component(c);
			if (component != null)
				out.add(component);
		}
		for (Entity entity : m_EntitiesToAdd) {
			T component = entity.<T>component(c);
			if (component != null)
				out.add(component);
		}
		return out;
	}

	@Override
	public String toString() {
		return string(0);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Layer {\n", m_Window.string(indentAmt + 1),
				StringTools.indentl(indentAmt + 1), "entities {\n", StringTools.buildString(indentAmt + 2, m_Entities),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt), "}");
	}

	private boolean m_Enabled;

	@Override
	public boolean enabled() {
		return m_Enabled;
	}

	@Override
	public void enable() {
		m_Enabled = true;
	}

	@Override
	public void disable() {
		m_Enabled = false;
	}

	/**
	 * @return m_Name
	 */
	public String name() {
		return m_Name;
	}

	/**
	 * @param m_Name m_Name to set
	 */
	public void name(String m_Name) {
		this.m_Name = m_Name;
	}

}
