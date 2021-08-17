package betrayal;

import engine.Application;
import engine.Driver;

public class Betrayal {

	public static void main(String[] args) {
		Driver d = new Driver();
		d.app(new Application("BETRAYAL", 2048, 1024, "res/betrayal/saves/betrayal.bsv"));
		d.start();
	}

}
