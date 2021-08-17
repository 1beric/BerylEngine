#version 400

in vec2 pass_TexCoords;

out vec4 out_Color;

uniform sampler2D tex;
uniform float brightness;

void main(void) {
	out_Color = texture(tex,pass_TexCoords) * brightness;
	out_Color.a = 1;
}
