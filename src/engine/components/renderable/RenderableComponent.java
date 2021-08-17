package engine.components.renderable;

import engine.components.Component;
import engine.entities.Entity;
import engine.models.Texture;
import engine.shaders.uniforms.Uniform;
import engine.util.Renderable;

public abstract class RenderableComponent extends Component implements Renderable {

	
	@Override public Texture render(Object... args) { return null; }
}
