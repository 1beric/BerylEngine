#version 400

in vec2 position;

out vec2 pass_TexCoords[11];

uniform float widthInPixels;

void main(void){
	gl_Position = vec4(position, 0.0, 1.0);
	vec2 centerTexCoords = position / 2.0 + 0.5;

	float pixelSize = 1.0 / widthInPixels;
	for (int i=-5; i<=5; i++) {
		pass_TexCoords[i+5] = centerTexCoords + vec2(pixelSize * i, 0);
	}

}
