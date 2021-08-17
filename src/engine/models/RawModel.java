package engine.models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class RawModel implements Stringable {

	private int m_VaoID;
	private int m_VertexCount;
	private ModelData m_Data;

	public RawModel(int vaoID, int vertexCount, ModelData data) {
		this.m_VaoID = vaoID;
		this.m_VertexCount = vertexCount;
		this.m_Data = data;
	}
	
	/**
	 * @return the vaoID
	 */
	public int vaoID() {
		return m_VaoID;
	}

	/**
	 * @return the vertexCount
	 */
	public int vertexCount() {
		return m_VertexCount;
	}

	/**
	 * @return the data
	 */
	public ModelData data() {
		return m_Data;
	}
	
	public void bind() {
		GL30.glBindVertexArray(m_VaoID);
	}
	
	public void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	public void draw() {
		switch (m_Data.getType()) {
		case POS_DIMS:
		case POS_TEXS:
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, m_VertexCount);
			break;
		case POS_TEXS_NORMS_INDS:
			GL11.glDrawElements(GL11.GL_TRIANGLES, m_VertexCount, GL11.GL_UNSIGNED_INT, 0);
			break;
		case POS_TEXS_NORMS_TANGS_INDS:
			System.out.println("UNSUPPORTED");
			break;
		case NOT_SET:
		default:
			break;
		}
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(
				StringTools.indent(indentAmt),
				"RawModel (id:"+m_VaoID+", count:"+m_VertexCount+")"
		);
	}

	@Override
	public String toString() {
		return string(0);
	}
	
}