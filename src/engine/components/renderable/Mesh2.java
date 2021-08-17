package engine.components.renderable;

import java.util.List;

import org.joml.Vector2f;

import engine.Updateable;
import engine.animation.Animatable;
import engine.models.RawModel;
import engine.models.Rect;
import engine.models.Texture;
import engine.util.string.StringTools;

public abstract class Mesh2 extends RenderableComponent implements Animatable, Updateable {

	public abstract List<Rect> allRects();

	public abstract Vector2f verticalBounds();

	public abstract Vector2f horizontalBounds();

	public abstract void translate(Vector2f translation);

	public abstract Mesh2 clone();

	@Override
	public Texture render(Object... args) {
		if (!enabled())
			return null;
		Vector2f resolution = (Vector2f) args[0];
		RawModel model = (RawModel) args[1];
		for (Rect rect : allRects())
			rect.render(resolution, model);
		return null;
	}

	public void screenTranslate(Vector2f translation, Vector2f res) {
		// put it in the top left
		this.translate(new Vector2f(-horizontalBounds().x, -verticalBounds().x));

		// calculate amount need to translate
		Vector2f screenTranslation = res.mul(translation);
		screenTranslation.sub(horizontalBounds().y * translation.x, verticalBounds().y * translation.y);

		// translate
		this.translate(screenTranslation);
	}

	public String toString() {
		return string(0);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), getClass().getSimpleName(), " {",
				StringTools.indentl(indentAmt + 1), "horizontal bounds: ", horizontalBounds().toString(),
				StringTools.indentl(indentAmt + 1), "vertical bounds: ", verticalBounds().toString(),
				StringTools.indentl(indentAmt), "}");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void lateUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
