uniform mat4   u_worldTransform;
uniform mat4   u_projectionMatrix;

attribute vec4 a_position;

void main() {
  gl_Position = u_projectionMatrix * u_worldTransform * a_position;
}