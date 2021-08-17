package engine.components.renderable;

import org.joml.Matrix4f;

import engine.Material;
import engine.Updateable;
import engine.animation.Animatable;
import engine.components.Component;
import engine.entities.Entity;
import engine.models.RawModel;
import engine.models.Texture;
import engine.models.creation.ModelFileType;
import engine.models.creation.OBJFileLoader;
import engine.shaders.Shader;
import engine.shaders.uniforms.MatrixUniform;
import engine.shaders.uniforms.Uniform;
import engine.util.string.StringTools;

public class Mesh3 extends RenderableComponent implements Animatable, Updateable {

	private Material m_Material;
	private RawModel m_Model;
	private Transform m_Transform;

	public Mesh3() {
		super();
		m_Material = Material.material("default3D");
	}

	public Mesh3(String file, ModelFileType type) {
		super();
		m_Material = Material.material("default3D");
		switch (type) {
		case OBJ:
			m_Model = OBJFileLoader.loadOBJ(file);
			break;
		}
	}

	public Mesh3(RawModel model) {
		super();
		m_Model = model;
		m_Material = Material.material("default3D");
	}

	@Override
	public Texture render(Object... args) {
		Camera cam = (Camera) args[0];
		Light light = (Light) args[1];
		Matrix4f projection = (Matrix4f) args[2];
		m_Model.bind();
		Shader shader = m_Material.shader();
		shader.start();
		if (light != null)
			light.load(shader);
		shader.<MatrixUniform>uniform("viewMatrix").val(cam.viewMatrix());
		shader.<MatrixUniform>uniform("projectionMatrix").val(projection);
		shader.<MatrixUniform>uniform("transformationMatrix").val(transformationMatrix());
		shader.loadUniforms();
		m_Model.draw();
		shader.unloadUniforms();
		shader.stop();
		m_Model.unbind();
		return null;
	}

	@Override
	public <T extends Uniform> T uniform(String name) {
		return m_Material.<T>uniform(name);
	}

	@Override
	public Component entity(Entity entity) {
		super.entity(entity);
		m_Transform = entity.<Transform>component(Transform.class);
		if (m_Transform == null) {
			m_Transform = new Transform();
			entity.addComponent(m_Transform);
		}
		return this;
	}

	public Material material() {
		return m_Material;
	}

	public Mesh3 material(Material material) {
		m_Material = material;
		return this;
	}

	public RawModel model() {
		return m_Model;
	}

	public Mesh3 model(RawModel model) {
		m_Model = model;
		return this;
	}

	public Matrix4f transformationMatrix() {
		return m_Transform.transformationMatrix();
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "Mesh3 {\n", m_Model.string(indentAmt + 1), "\n",
				m_Material.string(indentAmt + 1), StringTools.indentl(indentAmt), "}");
	}

	@Override
	public String toString() {
		return string(0);
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {

	}

	@Override
	public void lateUpdate() {

	}

	@Override
	public void destroy() {

	}

}
