varying vec3        v_texCoord;
varying vec4        v_position;

void main() {
  vec4 finalColor   = texture2D(u_skyMapTexture, vec2( u_skyMapProgress, clamp(1.0f - v_position.y, 0.0f, 1.0f) ));
  gl_FragColor      = finalColor;
}
