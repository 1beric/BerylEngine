package engine.models.creation;

import java.util.Random;

import org.joml.Vector2f;

import engine.models.Cubemap;
import engine.util.Color;

public class ProceduralSkybox {

	public static Color m_Space = Color.blue().mul(.015f);
	public static Color m_Star1 = new Color(1, .75f, .2f);
	public static Color m_Star2 = Color.white();
	public static Color m_Fog = new Color(.8f, .8f, 1);

	public static Vector2f m_FogSpan = new Vector2f(.35f, .8f);

	private static final int PX = 0;
	private static final int NX = 1;
	private static final int PY = 2;
	private static final int NY = 3;
	private static final int PZ = 4;
	private static final int NZ = 5;

	public static Cubemap generateMap(Vector2f resolution, String name) {
		return new Cubemap(name, generateTextures(resolution), resolution);
	}

	public static float[][] generateTextures(Vector2f resolution) {
		float[][] textures = new float[6][];
		// Create 6 different textures to use as skybox
		for (int i = 0; i < 6; i++) {
			textures[i] = genTop(resolution);
//			switch (i) {
//			case NY:
//				textures[i] = genBottom(resolution);
//				break;
//			case NX:
//			case PX:
//			case NZ:
//			case PZ:
//				textures[i] = genSide(resolution);
//				break;
//			case PY:
//				textures[i] = genTop(resolution);
//				break;
//			}
		}
		return textures;
	}

	private static float[] genTop(Vector2f resolution) {
		float[] pixels = new float[(int) (resolution.x * resolution.y * 4)];
		Random r = new Random();
		for (int x = 0; x < resolution.x; x++) {
			for (int y = 0; y < resolution.y; y++) {
				Color color = getColor();
				float randomValue = r.nextFloat();
				setPixel(pixels, (int) resolution.x, x, y, color);
				if (randomValue < 0.4f && x > 0 && y > 0) {
					setPixel(pixels, (int) resolution.x, x - 1, y, color);
					setPixel(pixels, (int) resolution.x, x, y - 1, color);
					setPixel(pixels, (int) resolution.x, x - 1, y - 1, color);
				}
			}
		}
		return pixels;
	}

	private static float[] genSide(Vector2f resolution) {
		float[] pixels = new float[(int) (resolution.x * resolution.y * 4)];
		Random r = new Random();
		Vector2f scaledBounds = m_FogSpan.mul(resolution.y, new Vector2f());
		for (int x = 0; x < resolution.x; x++) {
			for (int y = 0; y < resolution.y; y++) {
				Color color = getColor();
				if (y > scaledBounds.y) {
					setPixel(pixels, (int) resolution.x, x, y, m_Fog);
				} else if (y > scaledBounds.x) {
					float factor = (scaledBounds.y - y) / (scaledBounds.y - scaledBounds.x);
					color = m_Fog.interpolate(factor, color);
					float randomValue = r.nextFloat();
					setPixel(pixels, (int) resolution.x, x, y, color);
					if (randomValue < 0.4f && x > 0 && y > 0) {
						setPixel(pixels, (int) resolution.x, x - 1, y, color);
						setPixel(pixels, (int) resolution.x, x, y - 1, color);
						setPixel(pixels, (int) resolution.x, x - 1, y - 1, color);
					}
				} else {
					float randomValue = r.nextFloat();
					setPixel(pixels, (int) resolution.x, x, y, color);
					if (randomValue < 0.4f && x > 0 && y > 0) {
						setPixel(pixels, (int) resolution.x, x - 1, y, color);
						setPixel(pixels, (int) resolution.x, x, y - 1, color);
						setPixel(pixels, (int) resolution.x, x - 1, y - 1, color);
					}
				}
			}
		}
		return pixels;
	}

	private static float[] genBottom(Vector2f resolution) {
		float[] pixels = new float[(int) (resolution.x * resolution.y * 4)];
		for (int x = 0; x < resolution.x; x++) {
			for (int y = 0; y < resolution.y; y++) {
				setPixel(pixels, (int) resolution.x, x, y, m_Fog);
			}
		}
		return pixels;
	}

	private static Color getColor() {
		Random r = new Random();
		float randomValue = r.nextFloat();
		if (randomValue < 0.001f) {
			float dampener = 0.6f + r.nextFloat() * .4f; // base grey: [.6, 1]
			return m_Star1.mul(dampener);
		} else if (randomValue < 0.0015f) {
			float dampener = 0.6f + r.nextFloat() * .3f; // dampen: [.6, .9]
			return m_Star2.mul(dampener); // orange color
		} else
			return m_Space; // base color
	}

	private static void setPixel(float[] pixels, int width, int x, int y, Color color) {
		int index = 4 * (x + y * width);
		pixels[index + 0] = color.r();
		pixels[index + 1] = color.g();
		pixels[index + 2] = color.b();
		pixels[index + 3] = 1;
	}

}
