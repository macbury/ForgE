uniform vec4      u_eyePosition;
uniform vec4      u_skyColor;
uniform sampler2D u_diffuseTexture;
uniform sampler2D u_sunDepthMap;
uniform DirectionalLight u_mainLight;
varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying vec4   v_positionLightTrans;


void main() {
  float len           = length(v_position.xyz-u_mainLight.position.xyz) / u_mainLight.far;
  vec3 depth          = (v_positionLightTrans.xyz / u_mainLight.position.w) * 0.5f + 0.5f;
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  vec4 diffuse        = v_lightDiffuse * texture;

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
    diffuse.rgb = vec3(1.0f- len) ;//* v_lightDiffuse.rgb;
  #endif

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
}
