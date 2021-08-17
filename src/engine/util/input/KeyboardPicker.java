package engine.util.input;

import java.util.HashSet;
import java.util.Set;

import engine.events.Event;
import engine.events.keyboard.KeyPressedEvent;
import engine.events.keyboard.KeyReleasedEvent;

public class KeyboardPicker {

	private static Set<Integer> s_KeysDown;

	public static void init() {
		s_KeysDown = new HashSet<>();
	}

	public static void handleEvent(Event event) {
		if (event instanceof KeyPressedEvent) {
			KeyPressedEvent kpe = (KeyPressedEvent) event;
			if (kpe.repeatCount() == 0)
				s_KeysDown.add(kpe.key());
		} else if (event instanceof KeyReleasedEvent) {
			KeyReleasedEvent kre = (KeyReleasedEvent) event;
			s_KeysDown.remove(kre.key());
		}
	}

	public static boolean key(int key) {
		return s_KeysDown.contains(key);
	}

}
