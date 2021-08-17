package engine.util.lambdas;

import org.joml.Vector2f;

public interface Vec2Fn extends Lambda {

	public void handle(Object hovered, Vector2f coords);
	
}
