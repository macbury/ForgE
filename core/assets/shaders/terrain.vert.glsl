attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_material;
attribute vec2 a_texCoord0;

uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

uniform DirectionalLight u_mainLight;
uniform vec4             u_ambientLight;

varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;

void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  float ao          = a_material.r;
  float specular    = a_material.g;
  v_transparent     = a_material.b;

  vec3 lightDiffuse = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, v_normal) - vec3(ao, ao, ao);
  v_lightDiffuse    = vec4(lightDiffuse, 1f);
  v_textCoord       = a_texCoord0;
  v_position        = u_worldTransform * a_position;
  gl_Position       = u_projectionMatrix * v_position;
}
