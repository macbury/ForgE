#version 120

attribute vec4 a_position;
attribute vec4 a_color;
uniform mat4   u_projViewTrans;

varying vec4   v_color;

void main() {
  v_color     = a_color;
  gl_Position = u_projViewTrans * a_position;
}