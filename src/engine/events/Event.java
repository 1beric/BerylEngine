package engine.events;

public abstract class Event {

	private EventType m_EventType;
	private String m_Name;
	private int m_CategoryFlags;
	
	public boolean inCategory(EventCategory category) {
		return (m_CategoryFlags & category.val()) > 0;
	}
	
	public EventType type() {
		return m_EventType;
	}
	
	public Event type(EventType type) {
		m_EventType = type;
		return this;
	}
	
	public String name() {
		return m_Name;
	}

	public Event name(String name) {
		m_Name = name;
		return this;
	}
	
	public int categoryFlags() {
		return m_CategoryFlags;
	}
	
	public Event categoryFlags(int flags) {
		m_CategoryFlags = flags;
		return this;
	}
	
	@Override
	public String toString() {
		return m_Name; 
	}
	
}
