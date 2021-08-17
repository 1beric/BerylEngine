package engine.shaders.uniforms;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import engine.util.string.StringTools;

public class MatrixUniform extends Uniform {

	private Matrix4f m_Val;
	private FloatBuffer m_Fb;

	public MatrixUniform(String name, Matrix4f val) {
		super(name);
		m_Val = val;
		m_Fb = BufferUtils.createFloatBuffer(16);
	}

	public void load() {
		m_Val.get(m_Fb);
		GL20.glUniformMatrix4fv(location(), false, m_Fb);
	}

	public MatrixUniform val(Matrix4f val) {
		m_Val = val;
		return this;
	}

	public Matrix4f val() {
		return m_Val;
	}

	@Override
	public Uniform clone() {
		return new MatrixUniform(name(), new Matrix4f().set(m_Val));
	}

	@Override
	public String string(int indentAmt) {
		return StringTools.buildString(StringTools.indent(indentAmt), name(), " {", StringTools.indentl(indentAmt + 1),
				m_Val.m00() + ", " + m_Val.m10() + ", " + m_Val.m20() + ", " + m_Val.m30(),
				StringTools.indentl(indentAmt + 1),
				m_Val.m00() + ", " + m_Val.m11() + ", " + m_Val.m21() + ", " + m_Val.m31(),
				StringTools.indentl(indentAmt + 1),
				m_Val.m00() + ", " + m_Val.m12() + ", " + m_Val.m22() + ", " + m_Val.m32(),
				StringTools.indentl(indentAmt + 1),
				m_Val.m00() + ", " + m_Val.m13() + ", " + m_Val.m23() + ", " + m_Val.m33(),
				StringTools.indentl(indentAmt), "}");
	}

	@Override
	public String toWrite() {
		return "MatrixUniform;" + name() + ";identity";
	}

}
