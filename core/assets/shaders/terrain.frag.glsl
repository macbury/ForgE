uniform vec4      u_eyePosition;
uniform vec4      u_skyColor;
uniform sampler2D u_diffuseTexture;

varying vec4   v_color;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;

void main() {
  vec4 diffuse = texture2D(u_diffuseTexture, v_textCoord);

  gl_FragColor = fog(diffuse * v_color, u_skyColor, u_eyePosition, v_position);
}
