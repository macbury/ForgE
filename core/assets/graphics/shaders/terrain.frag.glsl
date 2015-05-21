#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif
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
  vec4 light          = vec4(v_lightDiffuse);
  vec3 depth          = (v_positionLightTrans.xyz / v_positionLightTrans.w) * 0.5f + 0.5f;
  if (v_positionLightTrans.z>=0.0 && (depth.x >= 0.0) && (depth.x <= 1.0) && (depth.y >= 0.0) && (depth.y <= 1.0) ) {
    float lenToLight    = length(v_position.xyz-u_mainLight.position.xyz)/u_mainLight.far;
    float lenDepthMap   = texture2D(u_sunDepthMap, depth.xy).a;

    if (lenDepthMap<lenToLight-0.005f) {
      light.rgb *= 0.4f;
    } else {
      light.rgb *= 0.4f + 0.6f*(1.0f-lenToLight);
    }
  } else {
    light.rgb *= 0.4f;
  }

  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  vec4 diffuse        = light * texture;
  /*float lenFB         = texture2D(u_sunDepthMap, depth.xy).a;
  float lenLight      = length(v_position.xyz-u_mainLight.position.xyz) / u_mainLight.far;

  float diff          = lenFB - lenLight;
  if(!(diff < 0.01f && diff > -0.01f)) {
    diffuse.rgb *= 0.4f;
  }*/

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = light.rgb;

    //diffuse.rgb = vec3(1.0f- len) ;//* v_lightDiffuse.rgb;
  #endif

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
}
