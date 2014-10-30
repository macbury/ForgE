uniform vec4   u_eyePosition;
uniform vec4   u_skyColor;
varying vec4   v_color;
varying vec3   v_normal;
varying vec4   v_position;

void main() {
  gl_FragColor = mix(v_color, u_skyColor, fogNormal(u_eyePosition, v_position));
}
