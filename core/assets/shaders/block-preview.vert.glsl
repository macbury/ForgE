attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_material;
attribute vec2 a_texCoord0;

uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

varying vec2   v_textCoord;
varying vec3   v_normal;
void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  v_textCoord       = a_texCoord0;
  vec4 v_position   = u_worldTransform * a_position;

  gl_Position       = u_projectionMatrix * v_position;
}
