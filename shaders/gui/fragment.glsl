#version 400

in vec2 pass_glPosition;
in vec2 pass_TexCoords;

out vec4 out_Color;

uniform sampler2D image;
uniform vec3 color;
uniform float opacity;
uniform float borderWidth;
uniform float borderRadius;
uniform vec3 borderColor;
uniform vec2 horizontalBounds;
uniform vec2 verticalBounds;
uniform vec2 resolution;

vec2 screenToPixel(vec2 screenPoint) {
	return vec2((screenPoint.x + 1) * resolution.x / 2.0, (screenPoint.y * -1.0 + 1) * (resolution.y / 2.0));
}

vec2 pixelToScreen(vec2 pixelPoint) {
	return vec2(pixelPoint.x * 2.0 / resolution.x - 1.0, pixelPoint.y * -2.0 / resolution.y + 1.0);
}

vec2 shrink(vec2 b, float s) {
	return vec2(b.x + s, b.y - s);
}

float betweenBounds(float p, vec2 b) {
	if (p >= b.x && p <= b.y)
		return 1.0;
	else
		return 0.0;
}

float withinBorder(vec2 pixelPoint, float width) {
	if (1 - betweenBounds(pixelPoint.x, shrink(horizontalBounds, width)) > 0.5 ||
			1 - betweenBounds(pixelPoint.y, shrink(verticalBounds, width)) > 0.5)
		return 1.0;
	else
		return 0.0;
}

float outsideRadius(vec2 pixelPoint, float width, float radius) {

	float left = betweenBounds(pixelPoint.x, vec2(horizontalBounds.x, horizontalBounds.x + width));
	float right = betweenBounds(pixelPoint.x, vec2(horizontalBounds.y - width, horizontalBounds.y));
	float top = betweenBounds(pixelPoint.y, vec2(verticalBounds.x, verticalBounds.x + width));
	float bottom = betweenBounds(pixelPoint.y, vec2(verticalBounds.y - width, verticalBounds.y));

	if (left > 0.5 && top > 0.5 && length(pixelPoint - vec2(horizontalBounds.x + width, verticalBounds.x + width)) > radius)
		return 1.0;
	else if (left > 0.5 && bottom > 0.5 && length(pixelPoint - vec2(horizontalBounds.x + width, verticalBounds.y - width)) > radius)
		return 1.0;
	else if (right > 0.5 && top > 0.5 && length(pixelPoint - vec2(horizontalBounds.y - width, verticalBounds.x + width)) > radius)
		return 1.0;
	else if (right > 0.5 && bottom > 0.5 && length(pixelPoint - vec2(horizontalBounds.y - width, verticalBounds.y - width)) > radius)
		return 1.0;
	else
		return 0.0;
}

void main(void) {

	vec2 pixelPosition = screenToPixel(pass_glPosition);

	if (outsideRadius(pixelPosition, borderRadius, borderRadius) > 0.5)
		discard;

	vec4 finalColor = texture(image, pass_TexCoords) * vec4(color, 1);
	float finalAlpha = opacity;

	float borderVal = clamp(withinBorder(pixelPosition, borderWidth) + outsideRadius(pixelPosition, borderRadius, borderRadius - borderWidth), 0, 1);
	finalColor = finalColor * (1 - borderVal) + vec4(borderColor,1) * borderVal;

	out_Color = vec4(finalColor.rgb, finalColor.a * finalAlpha);
}

