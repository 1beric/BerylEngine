package engine.components.updateable;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.components.renderable.Camera;
import engine.components.renderable.Transform;
import engine.util.Time;
import engine.util.input.KeyboardPicker;
import engine.util.input.MousePicker;
import engine.util.string.StringTools;

public class CameraController extends UpdateableComponent {

	private static final float SPEED = 1f;
	private static final float ROTATE_SPEED = 10000f;
	private static final int LEFT = GLFW.GLFW_KEY_A;
	private static final int RIGHT = GLFW.GLFW_KEY_D;
	private static final int UP = GLFW.GLFW_KEY_E;
	private static final int DOWN = GLFW.GLFW_KEY_Q;
	private static final int FORWARD = GLFW.GLFW_KEY_W;
	private static final int BACKWARD = GLFW.GLFW_KEY_S;
	private static final int MULTIPLIER_KEY = GLFW.GLFW_KEY_LEFT_SHIFT;
	private static final float MIN_ROTATE_X = -90;
	private static final float MAX_ROTATE_X = 90;

	private float m_Multiplier;

	private Camera m_Cam;
	private Transform m_Transform;
	private boolean m_Controlling;

	@Override
	public void init() {

		m_Cam = entity().<Camera>component(Camera.class);
		if (m_Cam == null)
			entity().addComponent(m_Cam = new Camera());
		m_Transform = entity().<Transform>component(Transform.class);
		m_Multiplier = 1;
	}

	@Override
	public void update() {
		if (!m_Controlling)
			return;
		updateMultiplier();
		rotate();
		move();
	}

	private void move() {
		Vector3f translation = new Vector3f(0);
		if (KeyboardPicker.key(UP))
			translation.add(m_Transform.up());
		if (KeyboardPicker.key(DOWN))
			translation.add(m_Transform.down());
		if (KeyboardPicker.key(RIGHT))
			translation.add(m_Transform.right());
		if (KeyboardPicker.key(LEFT))
			translation.add(m_Transform.left());
		if (KeyboardPicker.key(BACKWARD))
			translation.add(m_Transform.back());
		if (KeyboardPicker.key(FORWARD))
			translation.add(m_Transform.forward());
		translation.mul(SPEED * m_Multiplier * Time.delta());
		if (translation.length() > 0)
			m_Transform.translate(translation);
	}

	private void rotate() {
		Vector3f rotation = new Vector3f(0);
		Vector2f delta = MousePicker.deltaPosition();
		Vector2f res = entity().layer().window().resolution();
		rotation.add(new Vector3f(delta.y / res.y, delta.x / res.x, 0));
		rotation.mul(ROTATE_SPEED * Time.delta());
		if (rotation.length() > 0)
			m_Transform.rotate(rotation);
		if (m_Transform.rotation().x < MIN_ROTATE_X)
			m_Transform.rotate(new Vector3f(MIN_ROTATE_X - m_Transform.rotation().x, 0, 0));
		if (m_Transform.rotation().x > MAX_ROTATE_X)
			m_Transform.rotate(new Vector3f(MAX_ROTATE_X - m_Transform.rotation().x, 0, 0));
	}

	private void updateMultiplier() {
		if (KeyboardPicker.key(MULTIPLIER_KEY))
			m_Multiplier = Math.min(m_Multiplier + 10 * Time.delta(), 5);
		else
			m_Multiplier = Math.max(m_Multiplier - 10 * Time.delta(), 1);
	}

	public String toString() {
		return string(0);
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), "CameraController");
	}

	public CameraController controlling(boolean controlling) {
		m_Controlling = controlling;
		MousePicker.cursor(!controlling);
		return this;
	}

}
