package engine.components.renderable;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.Material;
import engine.models.RawModel;
import engine.models.Texture;
import engine.models.creation.OBJFileLoader;
import engine.shaders.Shader;
import engine.shaders.uniforms.MatrixUniform;
import engine.util.string.StringTools;

public class Transform extends RenderableComponent {

	private static RawModel m_Arrows;
	private static Material m_ArrowsMaterial;

	private Vector3f m_Position;
	private Vector3f m_Rotation;
	private Vector3f m_Scale;

	private Vector3f m_Forward;
	private Vector3f m_Up;
	private Vector3f m_Right;

	private static void initArrows() {
		m_Arrows = OBJFileLoader.loadOBJ("arrows");
		m_ArrowsMaterial = Material.material("arrows");
	}

	public Transform() {
		if (m_Arrows == null)
			initArrows();
		m_Position = new Vector3f(0);
		m_Rotation = new Vector3f(0);
		m_Scale = new Vector3f(1);
		m_Forward = new Vector3f(0, 0, -1);
		m_Up = new Vector3f(0, 1, 0);
		m_Right = new Vector3f(1, 0, 0);
	}

	public Transform translate(Vector3f translation) {
		m_Position.add(translation);
		return this;
	}

	public void updateDirections() {
		Vector3f radians = rotationRadians().mul(-1);
		m_Right = new Vector3f(1, 0, 0).rotateX(radians.x).rotateY(radians.y).rotateZ(radians.z)
				.mul((float) (180 / Math.PI)).normalize();
		m_Forward = new Vector3f(0, 0, -1).rotateX(radians.x).rotateY(radians.y).rotateZ(radians.z)
				.mul((float) (180 / Math.PI)).normalize();
		m_Up = new Vector3f(0, 1, 0).rotateX(radians.x).rotateY(radians.y).rotateZ(radians.z)
				.mul((float) (180 / Math.PI)).normalize();
	}

	@Override
	public Texture render(Object... args) {
		Camera cam = (Camera) args[0];
		Light light = (Light) args[1];
		Matrix4f projection = (Matrix4f) args[2];
		float distToCam = cam.position().sub(m_Position, new Vector3f()).length() / 10f;
		m_Arrows.bind();
		Shader shader = m_ArrowsMaterial.shader();
		shader.start();
		if (light != null)
			light.load(shader);
		shader.<MatrixUniform>uniform("viewMatrix").val(cam.viewMatrix());
		shader.<MatrixUniform>uniform("projectionMatrix").val(projection);
		shader.<MatrixUniform>uniform("transformationMatrix").val(transformationMatrixNoScale().scale(distToCam));
		shader.loadUniforms();
		m_Arrows.draw();
		shader.unloadUniforms();
		shader.stop();
		m_Arrows.unbind();
		return null;
	}

	/**
	 * @return m_Position
	 */
	public Vector3f position() {
		return m_Position;
	}

	/**
	 * @param m_Position m_Position to set
	 */
	public Transform position(Vector3f m_Position) {
		this.m_Position = m_Position;
		return this;
	}

	public Transform rotate(Vector3f rotation) {
		m_Rotation.add(rotation);
		updateDirections();
		return this;
	}

	/**
	 * @return m_Rotation
	 */
	public Vector3f rotation() {
		return m_Rotation;
	}

	/**
	 * @return m_Rotation in radians
	 */
	public Vector3f rotationRadians() {
		return m_Rotation.mul((float) Math.PI / 180, new Vector3f());
	}

	/**
	 * @param m_Rotation m_Rotation to set
	 */
	public Transform rotation(Vector3f m_Rotation) {
		this.m_Rotation = m_Rotation;
		updateDirections();
		return this;
	}

	/**
	 * @return m_Scale
	 */
	public Vector3f scale() {
		return m_Scale;
	}

	/**
	 * @param m_Scale m_Scale to set
	 */
	public Transform scale(Vector3f m_Scale) {
		this.m_Scale = m_Scale;
		return this;
	}

	public Vector3f up() {
		return m_Up;
	}

	public Vector3f right() {
		return m_Right;
	}

	public Vector3f forward() {
		return m_Forward;
	}

	public Vector3f down() {
		return m_Up.mul(-1, new Vector3f());
	}

	public Vector3f left() {
		return m_Right.mul(-1, new Vector3f());
	}

	public Vector3f back() {
		return m_Forward.mul(-1, new Vector3f());
	}

	public Matrix4f transformationMatrix() {
		Matrix4f matrix = new Matrix4f().identity();
		matrix = matrix.translate(m_Position);
		Vector3f rotation = rotationRadians();
		matrix = matrix.rotate(rotation.x, 1, 0, 0); // rotate x
		matrix = matrix.rotate(rotation.y, 0, 1, 0); // rotate y
		matrix = matrix.rotate(rotation.z, 0, 0, 1); // rotate z
		matrix = matrix.scale(m_Scale);
		return matrix;
	}

	public Matrix4f transformationMatrixNoScale() {
		Matrix4f matrix = new Matrix4f().identity();
		matrix = matrix.translate(m_Position);
		Vector3f rotation = rotationRadians();
		matrix = matrix.rotate(rotation.x, 1, 0, 0); // rotate x
		matrix = matrix.rotate(rotation.y, 0, 1, 0); // rotate y
		matrix = matrix.rotate(rotation.z, 0, 0, 1); // rotate z
		return matrix;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Transform {", StringTools.indentl(indentAmt + 1),
				"position: ", m_Position.toString(), StringTools.indentl(indentAmt + 1), "rotation: ",
				m_Rotation.toString(), StringTools.indentl(indentAmt + 1), "scale:    ", m_Scale.toString(),
				StringTools.indentl(indentAmt + 1), "forward:  ", m_Forward.toString(),
				StringTools.indentl(indentAmt + 1), "right:    ", m_Right.toString(),
				StringTools.indentl(indentAmt + 1), "up:       ", m_Up.toString(), StringTools.indentl(indentAmt), "}");
	}

}
