varying vec3        v_texCoord;
varying vec4        v_position;
void main() {
  if (v_position.y <= 0.0f) {
    gl_FragColor      = u_skyColor;
  } else {
    gl_FragColor      = vec4(v_position.xyz, 1.0f);
  }
}
