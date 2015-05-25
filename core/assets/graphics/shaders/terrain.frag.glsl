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
uniform vec4   u_ambientLight;

#define EPSILON 0.00001f


float unpack(vec4 packedZValue) {	
  const vec4 unpackFactors = vec4( 1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0 );
  return dot(packedZValue,unpackFactors);
}

vec4 calcShadow() {
  vec3 depth          = (v_positionLightTrans.xyz / v_positionLightTrans.w) * 0.5f + 0.5f;
  if (v_positionLightTrans.z >= 0.0 && (depth.x >= 0.0) && (depth.x <= 1.0) && (depth.y >= 0.0) && (depth.y <= 1.0) ) {
    float lenToLight    = length(v_position.xyz-u_mainLight.position.xyz)/u_mainLight.far;
    float lenDepthMap   = texture2D(u_sunDepthMap, depth.xy).a;

    if (lenDepthMap<lenToLight-0.005f) {
      float xOffset = 1.0f/1024.0f;
      float yOffset = 1.0f/1024.0f;
      float Factor = 0.0f;

      for (float y = -1.0f ; y <= 1.0f ; y++) {
        for (float x = -1.0f ; x <= 1.0f ; x++) {
          vec2 Offsets = vec2(x * xOffset, y * yOffset);
          Factor += texture2D(u_sunDepthMap, depth.xy + Offsets).a;
        }
      }

      vec4 c = vec4(0.5f + (Factor / 18.0f));
      return c * v_lightDiffuse;
    }
  } else {
    return v_lightDiffuse;
  }

  return v_lightDiffuse;
}

vec4 getTiledTexel() {
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  return texture2D(u_diffuseTexture, tilingTextCord);
}



void main() {
  
  vec4 diffuse        = getTiledTexel() * calcShadow();
  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  if (v_transparent >= 0.5f && diffuse.a <= 0.0f) {
    discard;
  } else {
    gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
  }
}
