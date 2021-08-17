#version 400 core

in vec3 textureCoords;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform samplerCube cubemap1;
//uniform samplerCube cubeMap2;
//uniform float blendFactor;
uniform vec3 fogColor;
uniform float lowerFog;
uniform float upperFog;

void main(void){
	vec4 tex1 = texture(cubemap1, textureCoords);
//	vec4 tex2 = texture(cubeMap2, textureCoords);
    vec4 finalColor = tex1; //mix(tex1,tex2,blendFactor);
    float factor = 0;
    if (upperFog - lowerFog != 0)
    	factor = (textureCoords.y - lowerFog) / (upperFog - lowerFog);
    factor = clamp(factor, 0.0, 1.0);
    out_Color = mix(vec4(fogColor,1.0),finalColor,factor);
    out_Color = finalColor;

	float brightness = (out_Color.r * 0.2126) + (out_Color.g * 0.7152) + (out_Color.b * 0.0722);
	if (brightness > 0.7) {
		out_BrightColor = out_Color;
	} else {
		out_BrightColor = vec4(0.0);
	}

}
