package betrayal.cards;

import engine.components.renderable.Mesh3;
import engine.models.Texture;
import engine.models.creation.ModelFileType;

public abstract class Card extends Mesh3 {

	private static final boolean STRING = true;

	public Card() {
		super("card", ModelFileType.OBJ);
	}

	protected abstract Texture texture();

	public abstract void read();

	@Override
	public void init() {
		initUniforms();
		super.init();
	}

	private void initUniforms() {
		uniform("modelTexture", texture());
		uniform("reflectivity", .2f);
		uniform("shineDamper", 1f);
	}

	@Override
	public String string(int indentAmt) {
		if (STRING)
			return super.string(indentAmt);
		return "";
	}

}
