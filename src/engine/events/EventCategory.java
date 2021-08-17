package engine.events;

public enum EventCategory {
	
	None		(0     ),
	Application	(1 << 0),
	Input		(1 << 1),
	Keyboard	(1 << 2),
	Mouse		(1 << 3),
	MouseButton	(1 << 4);
	
	private int val;
	
	EventCategory(int val) {
		this.val = val;
	}
	
	public int or(EventCategory other) {
		return this.val | other.val;
	}
	
	public int or(int other) {
		return this.val | other;
	}
	
	public int val() {
		return val;
	}
}