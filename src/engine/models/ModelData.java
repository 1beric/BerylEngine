package engine.models;

public class ModelData {

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private float[] tangents;
	private int[] indices;
	private int dimensions;
	private ModelDataType type;
	
	public ModelData() {
		this.vertices = new float[0];
		this.textureCoords = new float[0];
		this.normals = new float[0];
		this.tangents = new float[0];
		this.indices = new int[0];
		this.type = ModelDataType.NOT_SET;
	}
	
	public ModelData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.tangents = tangents;
		this.indices = indices;
		this.type = ModelDataType.POS_TEXS_NORMS_TANGS_INDS;
	}
	
	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.type = ModelDataType.POS_TEXS_NORMS_INDS;
	}

	public ModelData(float[] vertices, int dimensions) {
		this.vertices = vertices;
		this.dimensions = dimensions;
		this.type = ModelDataType.POS_DIMS;
	}

	public ModelData(float[] vertices, float[] texCoords) {
		this.vertices = vertices;
		this.textureCoords = texCoords;
		this.type = ModelDataType.POS_TEXS;
	}

	/**
	 * @return the vertices
	 */
	public float[] getVertices() {
		return vertices;
	}
	/**
	 * @return the textureCoords
	 */
	public float[] getTextureCoords() {
		return textureCoords;
	}
	/**
	 * @return the normals
	 */
	public float[] getNormals() {
		return normals;
	}
	/**
	 * @return the tangents
	 */
	public float[] getTangents() {
		return tangents;
	}
	/**
	 * @return the indices
	 */
	public int[] getIndices() {
		return indices;
	}
	/**
	 * @return the type
	 */
	public ModelDataType getType() {
		return type;
	}
	public int getDimensions() {
		return dimensions;
	}

}
