attribute vec3 a_normal;
attribute vec4 a_position;

varying vec3   v_normal;
varying vec4   v_position;
varying vec4   v_clipSpace;

void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  v_position        = u_worldTransform * a_position;
  v_clipSpace       = u_projectionMatrix * v_position;
  gl_Position       = v_clipSpace;
}
