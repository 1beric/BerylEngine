package engine.components;

import java.util.ArrayList;
import java.util.List;

import engine.components.renderable.Transform;
import engine.entities.Entity;
import engine.events.EventHandler;
import engine.util.Enableable;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public abstract class Component implements Stringable, EventHandler, Enableable {

	private static List<Component> s_Components = new ArrayList<>();

	public static <T> List<T> components(Class<?> type) {
		List<T> out = new ArrayList<>();
		for (Component c : s_Components)
			if (type.getSimpleName().contentEquals(c.getClass().getSimpleName()))
				out.add((T) c);
		return out;
	}

	private Entity m_Entity;
	protected boolean m_Enabled;

	public Component() {
		s_Components.add(this);
		m_Enabled = true;
	}

	public Entity entity() {
		return m_Entity;
	}

	public Component entity(Entity entity) {
		m_Entity = entity;
		return this;
	}

	public Transform transform() {
		return entity().<Transform>component(Transform.class);
	}

	public boolean enabled() {
		return m_Enabled;
	}

	public void enable() {
		m_Enabled = true;
	}

	public void disable() {
		m_Enabled = false;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.indent(indentAmt)
				+ (getClass().getSimpleName().contentEquals("") ? getClass().getSuperclass().getSimpleName()
						: getClass().getSimpleName())
				+ ": " + (m_Enabled ? "enabled" : "disabled");
	}

}
