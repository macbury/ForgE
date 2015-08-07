uniform mat4   u_mvpMatrix;
attribute vec4 a_position;
attribute vec2 a_texCoord0;
varying   vec2 v_texCoord;
void main() {
  v_texCoord    = a_texCoord0;
  //gl_Position   = u_worldTransform * u_projectionMatrix * a_position;
  gl_Position   = u_projectionMatrix * u_worldTransform * a_position;
}
