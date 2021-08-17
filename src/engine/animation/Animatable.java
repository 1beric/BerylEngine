package engine.animation;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.models.Cubemap;
import engine.models.Texture;
import engine.shaders.uniforms.BooleanUniform;
import engine.shaders.uniforms.ColorUniform;
import engine.shaders.uniforms.FloatUniform;
import engine.shaders.uniforms.MatrixUniform;
import engine.shaders.uniforms.Sampler2DUniform;
import engine.shaders.uniforms.SamplerCubeUniform;
import engine.shaders.uniforms.Uniform;
import engine.shaders.uniforms.Vec2Uniform;
import engine.shaders.uniforms.Vec3Uniform;
import engine.shaders.uniforms.Vec4Uniform;
import engine.util.Color;

public interface Animatable {

	public <T extends Uniform> T uniform(String name);
	
	public default void updateUniform(String name, Object value) {
		uniform(name, value);
	}
	
	public default void uniform(String name, Object value) {
		if (value instanceof Float) uniform(name, ((Float)value).floatValue());
		if (value instanceof Vector2f) uniform(name, (Vector2f)value);
		if (value instanceof Vector3f) uniform(name, (Vector3f)value);
		if (value instanceof Vector4f) uniform(name, (Vector4f)value);
		if (value instanceof Matrix4f) uniform(name, (Matrix4f)value);
		if (value instanceof Color) uniform(name, (Color)value);
		if (value instanceof Texture) uniform(name, (Texture)value);
	}
	
	public default FloatUniform uniform(String name, float value) {
		FloatUniform f = this.<FloatUniform>uniform(name);
		f.val(value);
		return f;
	}
	public default Vec2Uniform uniform(String name, Vector2f value) {
		Vec2Uniform u = this.<Vec2Uniform>uniform(name);
		u.val(value);
		return u;
	}
	public default Vec3Uniform uniform(String name, Vector3f value) {
		Vec3Uniform u = this.<Vec3Uniform>uniform(name);
		u.val(value);
		return u;
	}
	public default Vec4Uniform uniform(String name, Vector4f value) {
		Vec4Uniform u = this.<Vec4Uniform>uniform(name);
		u.val(value);
		return u;
	}
	public default MatrixUniform uniform(String name, Matrix4f value) {
		MatrixUniform u = this.<MatrixUniform>uniform(name);
		u.val(value);
		return u;
	}
	public default ColorUniform uniform(String name, Color value) {
		ColorUniform u = this.<ColorUniform>uniform(name);
		u.val(value);
		return u;
	}
	public default Sampler2DUniform uniform(String name, Texture value) {
		Sampler2DUniform u = this.<Sampler2DUniform>uniform(name);
		u.val(value);
		return u;
	}
	public default SamplerCubeUniform uniform(String name, Cubemap value) {
		SamplerCubeUniform u = this.<SamplerCubeUniform>uniform(name);
		u.val(value);
		return u;
	}
	public default BooleanUniform uniform(String name, boolean value) {
		BooleanUniform u = this.<BooleanUniform>uniform(name);
		u.val(value);
		return u;
	}
	
}
