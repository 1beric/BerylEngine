package engine.components.updateable;

import engine.Updateable;
import engine.components.Component;

public abstract class UpdateableComponent extends Component implements Updateable {

	public UpdateableComponent() {
		super();
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void lateUpdate() {
	}

	@Override
	public void destroy() {
	}
}
