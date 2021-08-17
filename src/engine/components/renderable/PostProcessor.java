package engine.components.renderable;

import java.util.ArrayList;
import java.util.List;

import engine.Updateable;
import engine.animation.Animatable;
import engine.models.RawModel;
import engine.models.Texture;
import engine.models.postprocessing.GaussianBlur;
import engine.models.postprocessing.PostProcessingEffect;
import engine.shaders.uniforms.Uniform;

public class PostProcessor extends RenderableComponent implements Animatable {

	private List<PostProcessingEffect> m_Effects;

	@Override
	public Texture render(Object... args) {
		Texture image = (Texture) args[0];
		RawModel model = (RawModel) args[1];
		if (m_Effects == null) m_Effects = new ArrayList<>();
		for (PostProcessingEffect effect : m_Effects)
			image = effect.render(image, model);
		return image;
	}
	
	public void addPPE(PostProcessingEffect ppe) {
		if (m_Effects == null) m_Effects = new ArrayList<>();
		m_Effects.add(ppe);
		ppe.postProcessor(this);
	}
	
	@Override
	public void updateUniform(String name, Object value) {
		for (PostProcessingEffect effect : m_Effects)
			effect.updateUniform(name, value);
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return null;
	}

}
