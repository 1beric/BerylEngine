#version 400

in vec2 pass_TexCoords[11];

out vec4 out_Color;

uniform sampler2D tex;

void main(void) {

	out_Color = vec4(0.0);
	out_Color += texture(tex, pass_TexCoords[0]) * 0.0093;
	out_Color += texture(tex, pass_TexCoords[1]) * 0.028002;
	out_Color += texture(tex, pass_TexCoords[2]) * 0.065984;
	out_Color += texture(tex, pass_TexCoords[3]) * 0.121703;
	out_Color += texture(tex, pass_TexCoords[4]) * 0.175713;
	out_Color += texture(tex, pass_TexCoords[5]) * 0.198596;
	out_Color += texture(tex, pass_TexCoords[6]) * 0.175713;
    out_Color += texture(tex, pass_TexCoords[7]) * 0.121703;
    out_Color += texture(tex, pass_TexCoords[8]) * 0.065984;
    out_Color += texture(tex, pass_TexCoords[9]) * 0.028002;
    out_Color += texture(tex, pass_TexCoords[10])* 0.0093;

}
