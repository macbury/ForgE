varying vec3   v_normal;
varying vec4   v_position;

void main() {
  vec4 diffuse = vec4(1.0f, 0.3f, 1.0f, 1.0f);
  gl_FragColor = applyFog(diffuse, v_position);
}
