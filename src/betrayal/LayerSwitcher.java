package betrayal;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import betrayal.controllers.BetrayalController;
import engine.components.Component;
import engine.components.updateable.UpdateableComponent;
import engine.events.keyboard.KeyPressedEvent;

public class LayerSwitcher extends UpdateableComponent {

	private List<BetrayalController> m_Controllers;

	private boolean m_MenuShowing = true;

	public LayerSwitcher() {
		super();
	}

	@Override
	public void init() {
		System.out.println(this.entity());
		m_Controllers = new ArrayList<>();
		m_Controllers.addAll(Component.<BetrayalController>components(BetrayalController.class));
	}

	public int start = 0;

	public void showHideMenu() {
		if (m_MenuShowing) {
			for (BetrayalController c : m_Controllers)
				c.hideMenu();
		} else {
			for (BetrayalController c : m_Controllers)
				c.showMenu();
		}
		m_MenuShowing = !m_MenuShowing;
	}

	@Override
	public boolean handleKeyPressed(KeyPressedEvent kpe) {
		if (kpe.key() == GLFW.GLFW_KEY_ESCAPE)
			showHideMenu();
		return false;
	}

}
