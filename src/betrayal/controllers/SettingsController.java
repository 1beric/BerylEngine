package betrayal.controllers;

public class SettingsController extends BetrayalController {

	@Override
	public void showMenu() {
		enable();
	}

	@Override
	public void hideMenu() {
		disable();
	}

	@Override
	public void init() {
		entity().layer().disable();
	}

}
