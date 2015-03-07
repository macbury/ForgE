attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_material;
attribute vec2 a_texCoord0;

uniform vec2   u_mapSize;
uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;
uniform float  u_time;
uniform DirectionalLight u_mainLight;
uniform vec4             u_ambientLight;

uniform sampler2D u_windDisplacementTexture;

varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_fogPower;
varying float  v_transparent;
varying float  v_waviness;

void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  float ao          = a_material.r;
  float specular    = a_material.g;
  v_waviness        = a_material.a;
  v_transparent     = a_material.b;

  vec3 lightDiffuse = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, v_normal) - vec3(ao, ao, ao);
  v_lightDiffuse    = vec4(lightDiffuse, 1f);
  v_textCoord       = a_texCoord0;
  v_position        = u_worldTransform * a_position;

  if (v_waviness >= 0.1f) {
    vec2 windUV  = vec2(v_position.x, v_position.z) / u_mapSize;
    vec4 texture = texture2D(u_windDisplacementTexture, vec2(windUV.x + u_time * 0.1f, windUV.y + u_time* 0.1f));
    v_position.x += texture.r * v_waviness;
    v_position.z += texture.g * v_waviness;
  }

  v_fogPower        = fogPowerByMapPosition(v_position, u_mapSize);

  gl_Position       = u_projectionMatrix * v_position;
}
