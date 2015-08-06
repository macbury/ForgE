uniform mat4   u_mvpMatrix;
attribute vec4 a_position;
varying vec3   v_texCoord;
varying vec4   v_position;
void main() {
  v_position    = normalize(a_position);
  v_texCoord    = a_position.xyz;
  gl_Position   = u_mvpMatrix * a_position;
}
