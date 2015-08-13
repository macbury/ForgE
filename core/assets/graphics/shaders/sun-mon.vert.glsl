attribute vec4 a_position;
attribute vec2 a_texCoord0;
varying   vec2 v_texCoord;
varying   vec4 v_position;
void main() {
  v_texCoord    = a_texCoord0;
  v_position    = u_worldTransform * a_position;
  gl_Position   = u_projectionMatrix * v_position;
}
