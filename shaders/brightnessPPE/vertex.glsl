#version 400

in vec2 position;

out vec2 pass_TexCoords;

void main(void){

	gl_Position = vec4(position, 0.0, 1.0);
	pass_TexCoords = position / 2.0 + 0.5;
}
