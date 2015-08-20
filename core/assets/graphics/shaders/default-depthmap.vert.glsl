attribute vec4 a_position;
varying vec4   v_position;
void main() {
  v_position        = u_worldTransform * vec4(a_position.xyz, 1.0f);
  gl_Position       = u_projectionMatrix * v_position;
}
