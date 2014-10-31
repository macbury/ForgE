uniform vec4   u_eyePosition;
uniform vec4   u_skyColor;
varying vec4   v_color;
varying vec3   v_normal;
varying vec4   v_position;

void main() {
  gl_FragColor = fog(v_color, u_skyColor, u_eyePosition, v_position);
}
