#version 120

attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_color;
uniform mat4   u_projectionMatrix;

varying vec4   v_color;

void main() {
  v_color     = a_color;
  gl_Position = u_projectionMatrix * a_position;
}