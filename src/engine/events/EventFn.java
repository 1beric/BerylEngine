package engine.events;

@FunctionalInterface
public interface EventFn {

	public boolean handle(Event event);
	
}
