uniform mat4   u_mvpMatrix;
attribute vec4 a_position;
varying vec3   v_texCoord;
void main() {
  v_texCoord = a_position.xyz;
  gl_Position = u_mvpMatrix * a_position;
}
