package engine.models;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.Material;
import engine.shaders.Shader;
import engine.shaders.uniforms.MatrixUniform;
import engine.shaders.uniforms.Vec2Uniform;
import engine.util.Renderable;
import engine.util.string.StringTools;
import engine.util.string.Stringable;

public class Rect implements Stringable, Renderable {

	private Material m_Material;

	public Rect(Vector2f horizontalBounds, Vector2f verticalBounds, Material material) {
		this.m_Material = material;
	}

	/**
	 * @return the m_HorizontalBounds
	 */
	public Vector2f horizontalBounds() {
		return m_Material.shader().<Vec2Uniform>uniform("horizontalBounds").val();
	}

	/**
	 * @param m_HorizontalBounds the m_HorizontalBounds to set
	 */
	public Rect horizontalBounds(Vector2f horizontalBounds) {
		m_Material.shader().<Vec2Uniform>uniform("horizontalBounds").val(horizontalBounds);
		return this;
	}

	/**
	 * @return the m_VerticalBounds
	 */
	public Vector2f verticalBounds() {
		return m_Material.shader().<Vec2Uniform>uniform("verticalBounds").val();
	}

	/**
	 * @param m_VerticalBounds the m_VerticalBounds to set
	 */
	public Rect verticalBounds(Vector2f verticalBounds) {
		m_Material.shader().<Vec2Uniform>uniform("verticalBounds").val(verticalBounds);
		return this;
	}

	/**
	 * @return the material
	 */
	public Material material() {
		return m_Material;
	}

	/**
	 * @param material the material to set
	 */
	public Rect material(Material material) {
		Vector2f hb = horizontalBounds();
		Vector2f vb = verticalBounds();
		this.m_Material = material;
		horizontalBounds(hb);
		verticalBounds(vb);
		return this;
	}

	/**
	 * @return screen space horziontal bounds
	 */
	public Vector2f screenSpaceHB(float widthInPixels) {
		return new Vector2f().set(horizontalBounds()).mul(2f / widthInPixels).sub(1, 1);
	}

	/**
	 * @return screen space vertical bounds
	 */
	public Vector2f screenSpaceVB(float heightInPixels) {
		return new Vector2f().set(verticalBounds()).mul(2f / heightInPixels).sub(1, 1).mul(-1);
	}

	public Matrix4f transformationMatrix(Vector2f resolution) {
		Vector2f hb = screenSpaceHB(resolution.x());
		Vector2f vb = screenSpaceVB(resolution.y());
		Matrix4f out = new Matrix4f().identity();
		out = out.translate(new Vector3f(hb.x(), vb.x(), 0));
		out = out.scale(new Vector3f(hb.y() - hb.x(), vb.y() - vb.x(), 1));
		return out;
	}

	public Rect translate(Vector2f translation) {
		horizontalBounds(horizontalBounds().add(new Vector2f(translation.x)));
		verticalBounds(verticalBounds().add(new Vector2f(translation.y)));
		return this;
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Rect {\n", m_Material.string(indentAmt + 1),
				StringTools.indentl(indentAmt), "}");
	}

	@Override
	public String toString() {
		return string(0);
	}

	@Override
	public Texture render(Object... args) {
		Vector2f resolution = (Vector2f) args[0];
		RawModel model = (RawModel) args[1];
		Shader shader = m_Material.shader();
		shader.start();
		((Vec2Uniform) shader.uniform("resolution")).val(resolution);
		((MatrixUniform) shader.uniform("transformationMatrix")).val(transformationMatrix(resolution));
		shader.loadUniforms();
		model.draw();
		shader.unloadUniforms();
		shader.stop();
		return null;
	}

}
