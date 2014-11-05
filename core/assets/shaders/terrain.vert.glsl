attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec4 a_material;

uniform mat3   u_normalMatrix;
uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

uniform DirectionalLight u_mainLight;
uniform vec4             u_ambientLight;

varying vec4   v_color;
varying vec3   v_normal;
varying vec4   v_position;

void main() {
  v_normal          = normalize(u_normalMatrix * a_normal);
  float ao          = a_material.r;

  vec3 lightDiffuse = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, v_normal) - vec3(ao, ao, ao);
  v_color           = vec4(a_color.rgb * lightDiffuse, a_color.a) ;

  #ifdef normalsDebugFlag
    v_color.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    v_color.rgb = lightDiffuse;
  #endif
  #ifdef materialDebugFlag
    v_color = a_material;
  #endif

  v_position        = u_worldTransform * a_position;
  gl_Position       = u_projectionMatrix * v_position;
}
