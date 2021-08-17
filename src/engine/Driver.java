package engine;

import engine.audio.AudioMaster;
import engine.util.Loader;
import engine.util.PlatformEntryPoint;
import engine.util.Time;
import engine.util.input.MousePicker;

public class Driver {

	static {
		System.setProperty("joml.format", "false");
	}

	private Application app;

	public void start() {
		init();
		loop();
		close();
	}

	public void app(Application app) {
		this.app = app;
	}

	private void init() {
		PlatformEntryPoint.initGLFW();
		app.init();
	}

	private void loop() {

		while (app.isRunning()) {
			Time.update();
			PlatformEntryPoint.errorCheckGL();
			app.update();
			app.lateUpdate();
			app.render();
			MousePicker.update();
			app.updateWindows();
			app.updateRunning();
		}

	}

	private void close() {
		app.destroy();
		Loader.cleanUp();
		AudioMaster.destroy();
		PlatformEntryPoint.destroyGLFW();
		PlatformEntryPoint.destroyGL();
	}

}
