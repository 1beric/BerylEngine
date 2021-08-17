package engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

public class AudioMaster {

	private static long s_Context;
	private static List<Integer> s_Buffers = new ArrayList<>();

	public static void init() {
		// Can call "alc" functions at any time
		long device = ALC10.alcOpenDevice((ByteBuffer) null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);

		s_Context = ALC10.alcCreateContext(device, (IntBuffer) null);
		ALC10.alcMakeContextCurrent(s_Context);
		AL.createCapabilities(deviceCaps);
		// Can now call "al" functions
	}

	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		s_Buffers.add(buffer);
		return buffer;
	}

	public static void destroy() {
		long device = ALC10.alcGetContextsDevice(s_Context);
		ALC10.alcCloseDevice(device);
		ALC.destroy();
	}

}
