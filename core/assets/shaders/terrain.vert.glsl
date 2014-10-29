attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_color;

uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

uniform DirectionalLight u_mainLight;
uniform vec4             u_ambientLight;

varying vec4   v_color;
varying vec3   v_normal;
varying vec3   v_lightDiffuse;

void main() {
  v_color        = a_color;
  v_normal       = normalize(u_normalMatrix * a_normal);
  v_lightDiffuse = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, v_normal);
  gl_Position    = u_projectionMatrix * u_worldTransform * a_position;
}