attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_material;
attribute vec2 a_texCoord0;
attribute vec4 a_textureFullCoords;

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

  vec3 lightDiffuse = applySunLight(v_normal);
  v_lightDiffuse    = u_ambientLight + vec4(lightDiffuse, 1.0f);
  v_textCoord       = a_texCoord0;
  v_position        = u_worldTransform * a_position;
  v_position        = applyWind(waviness, v_position);

  gl_Position       = u_projectionMatrix * v_position;
}
