package engine.models.creation;

import engine.models.ModelData;
import engine.models.RawModel;
import engine.util.Loader;

public class PlaneBuilder {

	private static final float[] D_VERTICES = new float[] {
		     -1,  0, -1,
		      1,  0,  1,
		     -1,  0,  1,
		      1,  0,  1,			// POS Y
		     -1,  0, -1,
		      1,  0, -1,

		      1,  0,  1,
		      1,  0, -1,
		     -1,  0,  1,
		      1,  0, -1,			// NEG Y
		     -1,  0, -1,
		     -1,  0,  1,
	};
	private static final float[] D_TEXTURE_COORDS = new float[] {
			0, 0,	// BOTTOM LEFT
			1, 1,	// TOP RIGHT
			0, 1,	// TOP LEFT
			1, 1,	// TOP RIGHT
			0, 0,	// BOTTOM LEFT
			1, 0,	// BOTTOM RIGHT
			
			1, 0,	// BOTTOM RIGHT
			1, 1,	// TOP RIGHT
			0, 0,	// BOTTOM LEFT
			1, 1,	// TOP RIGHT
			0, 1,	// TOP LEFT
			0, 0,	// BOTTOM LEFT
	};
	private static final float[] D_NORMALS = new float[] {
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y

			0,  -1,   0,		// NEG Y
			0,  -1,   0,		// NEG Y
			0,  -1,   0,		// NEG Y
			0,  -1,   0,		// NEG Y
			0,  -1,   0,		// NEG Y
			0,  -1,   0,		// NEG Y
	};	
	private static final int[] D_INDICES = new int[] {
			0,  1,  2,
			3,  4,  5,
			
			6,  7,  8,
			9,  10, 11,
	};
	
	private static final float[] VERTICES = new float[] {
		     -1,  0, -1,
		      1,  0,  1,
		     -1,  0,  1,
		      1,  0,  1,			// POS Y
		     -1,  0, -1,
		      1,  0, -1,
	};
	private static final float[] TEXTURE_COORDS = new float[] {
			0, 0,	// BOTTOM LEFT
			1, 1,	// TOP RIGHT
			0, 1,	// TOP LEFT
			1, 1,	// TOP RIGHT
			0, 0,	// BOTTOM LEFT
			1, 0,	// BOTTOM RIGHT
	};
	private static final float[] NORMALS = new float[] {
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
			0,   1,   0,		// POS Y
	};	
	private static final int[] INDICES = new int[] {
			0,  1,  2,
			3,  4,  5,
	};
	
	public static final RawModel build(boolean doubleSided) {
		if (doubleSided)
			return Loader.loadToVAO(new ModelData(D_VERTICES, D_TEXTURE_COORDS, D_NORMALS, D_INDICES));
		else
			return Loader.loadToVAO(new ModelData(VERTICES, TEXTURE_COORDS, NORMALS, INDICES));

	}
	
}
