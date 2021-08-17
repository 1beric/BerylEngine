package gameplay;

import engine.Application;
import engine.Driver;

public class App {

	private enum Game {
		TARGET_PRACTICE, SPHERE_BUILDER
	}

	private static final Game APPLICATION = Game.SPHERE_BUILDER;

	public static void main(String[] args) {
		Driver d = new Driver();
		switch (APPLICATION) {
		case TARGET_PRACTICE:
			d.app(new Application("Target Practice", 2048, 1024, "res/saves/targetPractice.bsv"));
			break;
		case SPHERE_BUILDER:
			d.app(new Application("Sphere Builder", 2048, 1024, "res/saves/sphereBuilder.bsv"));
			break;
		default:
			System.exit(0);
		}
		d.start();
	}

}
