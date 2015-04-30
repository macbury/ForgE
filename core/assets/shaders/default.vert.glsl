uniform mat4   u_worldTransform;
uniform mat4   u_projectionMatrix;
uniform mat3   u_normalMatrix;

uniform DirectionalLight u_mainLight;
uniform vec4   u_ambientLight;

uniform vec4   u_colorDiffuse;

attribute vec4 a_position;
attribute vec3 a_normal;

varying vec4   v_lightDiffuse;
varying vec4   v_color;
varying vec3   v_normal;
varying vec4   v_position;

void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  v_color           = u_colorDiffuse;

  vec3 lightDiffuse = directionalLightDiffuse(u_mainLight, v_normal);
  v_lightDiffuse    = u_ambientLight + vec4(lightDiffuse, 1.0f);
  v_position        = u_worldTransform * a_position;
  gl_Position       = u_projectionMatrix * v_position;
}
