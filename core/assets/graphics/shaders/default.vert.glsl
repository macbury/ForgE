attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

varying vec4   v_lightDiffuse;
varying vec4   v_color;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCord;
varying vec4   v_positionInLightSpace;
void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  v_color           = u_colorDiffuse;
  v_textCord        = u_diffuseUVTransform.xy + a_texCoord0 * u_diffuseUVTransform.zw;
  vec3 lightDiffuse = directionalLightDiffuse(u_mainLight, v_normal);
  v_lightDiffuse    = u_ambientLight + vec4(lightDiffuse, 1.0f);
  v_position        = u_worldTransform * a_position;

  v_positionInLightSpace = u_shadowMap.farTransform * vec4(v_position.xyz, 1.0f);

  gl_Position       = u_projectionMatrix * v_position;
}
