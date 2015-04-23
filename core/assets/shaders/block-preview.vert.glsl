attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_material;
attribute vec2 a_texCoord0;
attribute vec4 a_textureFullCoords;

uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

varying vec2   v_textCoord;
varying vec3   v_normal;

varying vec2   v_uvStart;
varying vec2   v_uvMul;

void main() {
  v_uvStart         = a_textureFullCoords.xy;
  v_uvMul           = a_textureFullCoords.zw - v_uvStart;
  
  v_normal          = normalize(u_normalMatrix * a_normal);
  v_textCoord       = a_texCoord0;
  vec4 v_position   = u_worldTransform * a_position;

  gl_Position       = u_projectionMatrix * v_position;
}
