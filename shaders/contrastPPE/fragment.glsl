#version 400

in vec2 pass_TexCoords;

out vec4 out_Color;

uniform sampler2D tex;
uniform float contrast;

void main(void) {

	vec4 color = texture(tex,pass_TexCoords);
	out_Color.a = color.a;
	out_Color.rgb = (color.rgb - 0.5) * (1.0 + contrast) + 0.5;

}
