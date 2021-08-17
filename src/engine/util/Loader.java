package engine.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import engine.models.ModelData;
import engine.models.RawModel;

public class Loader {

	private static Set<Integer> vaos = new HashSet<>();
	private static List<Integer> vbos = new ArrayList<>();
	private static List<Integer> textures = new ArrayList<>();

	private static final String[] s_CubemapFiles = new String[] { "right", "left", "top", "bottom", "back", "front" };

	public static RawModel loadToVAO(ModelData data) {
		int vaoID;
		switch (data.getType()) {
		case POS_TEXS_NORMS_INDS:
			// currently used with the entity shader
			vaoID = createVAO();
			bindIndicesBuffer(data.getIndices());
			storeDataInAttributeList(0, 3, data.getVertices());
			storeDataInAttributeList(1, 2, data.getTextureCoords());
			storeDataInAttributeList(2, 3, data.getNormals());
			unbindVAO();
			return new RawModel(vaoID, data.getIndices().length, data);
		case POS_TEXS:
			// unused
			vaoID = createVAO();
			storeDataInAttributeList(0, 2, data.getVertices());
			storeDataInAttributeList(1, 2, data.getTextureCoords());
			unbindVAO();
			return new RawModel(vaoID, data.getVertices().length / 2, data);
		case POS_TEXS_NORMS_TANGS_INDS:
			// used with a normal map
			vaoID = createVAO();
			bindIndicesBuffer(data.getIndices());
			storeDataInAttributeList(0, 3, data.getVertices());
			storeDataInAttributeList(1, 2, data.getTextureCoords());
			storeDataInAttributeList(2, 3, data.getNormals());
			storeDataInAttributeList(3, 3, data.getTangents());
			unbindVAO();
			return new RawModel(vaoID, data.getIndices().length, data);
		case POS_DIMS:
			vaoID = createVAO();
			storeDataInAttributeList(0, data.getDimensions(), data.getVertices());
			unbindVAO();
			return new RawModel(vaoID, data.getVertices().length / data.getDimensions(), data);
		case NOT_SET:
			return null;
		default:
			return null;
		}
	}

	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private static void storeDataInAttributeList(int attrNum, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attrNum, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private static void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static int loadCubemap(float[][] pixels, Vector2f resolution) {
		int id = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, id);
		for (int i = 0; i < pixels.length; i++) {
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, (int) resolution.x,
					(int) resolution.y, 0, GL11.GL_RGBA, GL11.GL_FLOAT, pixels[i]);
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		textures.add(id);
		return id;
	}

	public static int loadCubemap(String filepath) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		for (int i = 0; i < 6; i++) {
			Object[] data = decodeTextureFile("res/skyboxes/" + filepath + "/" + s_CubemapFiles[i] + ".png");
			if ((int) data[3] == 4)
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, (int) data[1],
						(int) data[2], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) data[0]);
			else
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGB, (int) data[1], (int) data[2],
						0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) data[0]);

			STBImage.stbi_image_free((ByteBuffer) data[0]);
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		textures.add(texID);
		return texID;
	}

	public static Object[] decodeTextureFile(String file) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = STBImage.stbi_load(file, width, height, channels, 0);
		// TODO image skybox not working
		if (image != null) {
			image.flip();

//			System.out.println(image.capacity());
			return new Object[] { image, width.get(0), height.get(0), channels.get(0) };
		} else
			System.out.println("error in image loading: " + file);

		return new Object[] { null, 0, 0, 0 };
	}

	public static void cleanUp() {
		for (int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for (int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
		for (int texture : textures)
			GL11.glDeleteTextures(texture);
	}

	public static void unloadVAO(int vao) {
		GL30.glDeleteVertexArrays(vao);
		vaos.remove((Integer) vao);
	}

}
