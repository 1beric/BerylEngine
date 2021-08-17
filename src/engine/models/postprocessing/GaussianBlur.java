package engine.models.postprocessing;

import org.joml.Vector2f;

import engine.models.RawModel;
import engine.models.Texture;

public class GaussianBlur extends PostProcessingEffect {

	private HorizontalGaussianBlur hgbPPE;
	private VerticalGaussianBlur vgbPPE;
	
	public GaussianBlur(Vector2f resolution, float scale) {
		super();
		hgbPPE = new HorizontalGaussianBlur(resolution, scale);
		vgbPPE = new VerticalGaussianBlur(resolution, scale);
	}

	@Override public void updateUniforms() {}

	@Override
	public Texture render(Object... args) {
		Texture image = (Texture) args[0];
		RawModel model = (RawModel) args[1];
		if (!enabled()) return image;
		return vgbPPE.render(hgbPPE.render(image, model), model);
	}

	@Override
	public void updateUniform(String name, Object value) {
		hgbPPE.updateUniform(name, value);
		vgbPPE.updateUniform(name, value);
	}

}
