package engine.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import engine.Layer;
import engine.Updateable;
import engine.components.Component;
import engine.components.renderable.Transform;
import engine.events.Event;
import engine.events.EventHandler;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Entity implements Updateable, Stringable, EventHandler {

	private static Map<String, Entity> s_Entities = new HashMap<>();

	public static Entity entity(String name) {
		return s_Entities.get(name);
	}

	private String m_Name;
	private Layer m_Layer;
	private boolean m_Selected;
	private boolean m_Enabled;
	private boolean m_Initialized;

	private List<Component> m_Components, m_ComponentsToAdd, m_ComponentsToRemove;

	public Entity(String name) {
		m_Name = name;
		s_Entities.put(name, this);
		m_Components = new ArrayList<>();
		m_ComponentsToAdd = new ArrayList<>();
		m_ComponentsToRemove = new ArrayList<>();
		m_Selected = false;
		m_Enabled = false;
		m_Initialized = false;
		m_Components.add(new Transform());
	}

	public Entity(String name, Vector3f position) {
		m_Name = name;
		s_Entities.put(name, this);
		m_Components = new ArrayList<>();
		m_ComponentsToAdd = new ArrayList<>();
		m_ComponentsToRemove = new ArrayList<>();
		m_Selected = false;
		m_Enabled = false;
		m_Initialized = false;
		Transform t = new Transform();
		t.position(position);
		m_Components.add(t);
	}

	public Entity(String name, Vector3f position, Vector3f rotation) {
		m_Name = name;
		s_Entities.put(name, this);
		m_Components = new ArrayList<>();
		m_ComponentsToAdd = new ArrayList<>();
		m_ComponentsToRemove = new ArrayList<>();
		m_Selected = false;
		m_Enabled = false;
		m_Initialized = false;
		Transform t = new Transform();
		t.position(position);
		t.rotation(rotation);
		m_Components.add(t);

	}

	public Entity(String name, Vector3f position, Vector3f rotation, Vector3f scale) {
		m_Name = name;
		s_Entities.put(name, this);
		m_Components = new ArrayList<>();
		m_ComponentsToAdd = new ArrayList<>();
		m_ComponentsToRemove = new ArrayList<>();
		m_Selected = false;
		m_Enabled = false;
		m_Initialized = false;
		Transform t = new Transform();
		t.position(position);
		t.rotation(rotation);
		t.scale(scale);
		m_Components.add(t);
	}

	@Override
	public void init() {
		handleAdds();
		handleRemoves();
		for (Component component : m_Components)
			if (component instanceof Updateable)
				((Updateable) component).init();
		m_Initialized = true;
		m_Enabled = true;
	}

	@Override
	public void update() {
		handleAdds();
		handleRemoves();
		if (!m_Enabled)
			return;
		for (Component component : m_Components)
			if (component instanceof Updateable) {
				((Updateable) component).update();
			}
	}

	@Override
	public void lateUpdate() {
		handleAdds();
		handleRemoves();
		if (!m_Enabled)
			return;
		for (Component component : m_Components)
			if (component instanceof Updateable)
				((Updateable) component).lateUpdate();
	}

	public boolean handleAllEvents(Event event) {
		if (!m_Enabled)
			return false;
		for (Component component : m_Components)
			if (component.handleEvent(event))
				return true;
		return false;
	}

	public Entity addComponent(Component component) {
		if (component == null)
			System.out.println("Component added was null");
		m_ComponentsToAdd.add(component);
		return this;
	}

	public Entity removeComponent(Component component) {
		m_ComponentsToRemove.add(component);
		return this;
	}

	public Transform transform() {
		return this.<Transform>component(Transform.class);
	}

	public <T extends Component> T component(Class<?> c) {
		for (Component component : m_Components)
			if (c.getSimpleName().contentEquals(component.getClass().getSimpleName()))
				return (T) component;
		for (Component component : m_ComponentsToAdd)
			if (c.getSimpleName().contentEquals(component.getClass().getSimpleName()))
				return (T) component;
		return null;
	}

	public <T extends Component> List<T> components(Class<?> c) {
		List<T> out = new ArrayList<>();
		for (Component component : m_Components)
			if (c.getSimpleName().contentEquals(component.getClass().getSimpleName()))
				out.add((T) component);
		return out;
	}

	private void handleAdds() {
		List<Component> copy = new ArrayList<>(m_ComponentsToAdd);
		m_ComponentsToAdd.clear();
		for (Component c : copy) {
			m_Components.add(c);
			c.entity(this);
			if (m_Initialized && c instanceof Updateable)
				((Updateable) c).init();
		}
	}

	private void handleRemoves() {
		List<Component> copy = new ArrayList<>(m_ComponentsToRemove);
		m_ComponentsToRemove.clear();
		for (Component c : copy) {
			m_Components.remove(c);
			if (c instanceof Updateable)
				((Updateable) c).destroy();
		}
	}

	@Override
	public void destroy() {
		handleAdds();
		handleRemoves();
		for (Component component : m_Components)
			if (component instanceof Updateable)
				((Updateable) component).destroy();
	}

	public String name() {
		return m_Name;
	}

	public Layer layer() {
		return m_Layer;
	}

	public Entity layer(Layer layer) {
		m_Layer = layer;
		return this;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), " {", StringTools.indentl(indentAmt + 1),
				m_Enabled ? "enabled" : "disabled", StringTools.indentl(indentAmt + 1),
				m_Selected ? "selected" : "not selected", StringTools.indentl(indentAmt + 1),
				m_Initialized ? "initialized" : "not initialized", StringTools.indentl(indentAmt + 1),
				"components: {\n", StringTools.buildString(indentAmt + 2, m_Components),
				StringTools.indentl(indentAmt + 1), "}", StringTools.indentl(indentAmt), "}");
	}

	@Override
	public String toString() {
		return string(0);
	}

}
