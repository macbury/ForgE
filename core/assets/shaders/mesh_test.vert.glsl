#version 120

attribute vec4 a_position;
uniform mat4   u_projViewTrans;
void main() {
  gl_Position = u_projViewTrans * a_position;
}