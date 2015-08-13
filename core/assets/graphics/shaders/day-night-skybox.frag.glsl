varying vec3        v_texCoord;
varying vec4        v_position;

float random(vec2 ab) {
	float f = (cos(dot(ab ,vec2(21.9898f,78.233f))) * 43758.5453f);
	return fract(f);
}

float noise(in vec2 xy) {
	vec2 ij = floor(xy);
	vec2 uv = xy-ij;
	uv = uv*uv*(3.0f-2.0f*uv);

	float a = random(vec2(ij.x, ij.y ));
	float b = random(vec2(ij.x+1., ij.y));
	float c = random(vec2(ij.x, ij.y+1.));
	float d = random(vec2(ij.x+1., ij.y+1.));
	float k0 = a;
	float k1 = b-a;
	float k2 = c-a;
	float k3 = a-b-c+d;
	return (k0 + k1*uv.x + k2*uv.y + k3*uv.x*uv.y);
}

void main() {
  vec4 finalColor   = texture2D(u_skyMapTexture, vec2( u_skyMapProgress, clamp(1.0f - v_position.y, 0.0f, 1.0f) ));
  gl_FragColor      = finalColor;
  //float color = pow(noise(v_texCoord.xy), 40.0f) * 20.0f;

	//float r1 = noise(v_texCoord.xy*noise(vec2(sin(u_time*0.02f))));

  //gl_FragColor      = vec4(vec3(color*r1, color*r1, color*r1), 1.0f);
  //gl_FragColor = vec4(random((v_texCoord.xy), 0.0f, 0.0f, 1.0f);
}
