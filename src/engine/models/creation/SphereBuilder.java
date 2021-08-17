package engine.models.creation;

import engine.models.ModelData;
import engine.models.RawModel;
import engine.util.Loader;

public class SphereBuilder {

	// soft edge version
	public static final RawModel build(int lod) {
		// build vertices!
		int numVertices = lod * lod + 2;
		int numTris = lod * lod * 2;

		float[] vertices = new float[numVertices * 3];
		float[] textureCoords = new float[numVertices * 2];
		int[] indices = new int[numTris * 3];

		float deltaTheta = (float) (Math.PI / (lod + 2));
		float deltaPhi = (float) (Math.PI * 2 / lod);
		float theta = 0;
		int vpos = 0;
		int tpos = 0;
		addVec(vertices, vpos, 0, 0, 1);
		for (int i = 0; i < lod - 1; i++)
			addInds(indices, tpos++, 0, i + 1, i + 2);
		addInds(indices, tpos++, 0, lod - 1, 1);
		vpos += 1;
		for (int ring = 0; ring < lod; ring++) {
			theta += deltaTheta;
			float phi = 0;
			for (int point = 0; point < lod; point++) {
				phi += deltaPhi;
				float x = (float) (Math.sin(theta) * Math.cos(phi));
				float y = (float) (Math.sin(theta) * Math.sin(phi));
				float z = (float) Math.cos(theta);
				addVec(vertices, vpos, x, y, z);
				if (ring < lod - 1 && point < lod - 1) {
					addInds(indices, tpos, vpos, vpos + lod + 1, vpos + 1);
					addInds(indices, tpos + 1, vpos, vpos + lod, vpos + lod + 1);
					tpos += 2;
				}
				vpos += 1;
			}
			vpos--;
			if (ring < lod - 1) {
				addInds(indices, tpos, vpos, vpos + 1, vpos - lod + 1);
				addInds(indices, tpos + 1, vpos, vpos + lod, vpos + 1);
				tpos += 2;
			}
			vpos++;
		}
		addVec(vertices, vpos, 0, 0, -1);
		for (int i = 0; i < lod - 1; i++)
			addInds(indices, tpos++, vpos - lod + i, vpos, vpos - lod + i + 1);
		addInds(indices, tpos++, vpos - 1, vpos, vpos - lod);
		vpos += 1;

		return Loader.loadToVAO(new ModelData(vertices, textureCoords, vertices, indices));
	}

	private static void addInds(int[] a, int tpos, int v1, int v2, int v3) {
		a[tpos * 3 + 0] = v1;
		a[tpos * 3 + 1] = v3;
		a[tpos * 3 + 2] = v2;
	}

	private static void addVec(float[] a, int vpos, float x, float y, float z) {
		a[vpos * 3 + 0] = x;
		a[vpos * 3 + 1] = y;
		a[vpos * 3 + 2] = z;
	}
}
