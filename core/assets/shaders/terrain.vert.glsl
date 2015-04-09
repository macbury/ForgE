attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_material;
attribute vec2 a_texCoord0;
attribute vec4 a_textureFullCoords;
uniform vec2   u_mapSize;
uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;
uniform float  u_time;
uniform DirectionalLight u_mainLight;
uniform vec4             u_ambientLight;

uniform sampler2D u_windDisplacementTexture;
uniform vec2      u_windDirection;

varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;

varying vec2   v_textCoord;
varying vec2   v_uvStart;
varying vec2   v_uvMul;

varying float  v_transparent;

void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  float ao          = a_material.r;
  float specular    = a_material.g;
  float waviness    = a_material.a;
  v_transparent     = a_material.b;

  v_uvStart         = a_textureFullCoords.xy;
  v_uvMul           = a_textureFullCoords.zw - v_uvStart;

  vec3 lightDiffuse = directionalLightDiffuse(u_mainLight, v_normal);
  v_lightDiffuse    = u_ambientLight + vec4(lightDiffuse, 1f);
  v_textCoord       = a_texCoord0;
  v_position        = u_worldTransform * a_position;
  v_position        = applyWind(u_time, u_windDirection, waviness, v_position, u_mapSize, u_windDisplacementTexture);

  gl_Position       = u_projectionMatrix * v_position;
}
