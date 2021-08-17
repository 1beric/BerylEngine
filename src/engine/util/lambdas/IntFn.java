package engine.util.lambdas;

import org.joml.Vector2f;

public interface IntFn extends Lambda {

	public void handle(Object clicked, int button, Vector2f coords);
	
}
