#version 400

in vec2 position;

out vec2 pass_glPosition;
out vec2 pass_TexCoords;

uniform mat4 transformationMatrix;

void main(void){

	gl_Position = transformationMatrix * vec4(position.x, position.y, 0.0, 1.0);
	pass_glPosition = gl_Position.xy;
	pass_TexCoords = position;
}
