varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying vec4   v_positionLightTrans;


float shadowCalculation(vec3 normal, vec3 lightDir, vec4 fragPosLightSpace, sampler2D depthMap) {
  float bias         = max(0.05f * (1.0f - dot(normal, lightDir)), 0.005f);
  vec3 projCoords    = fragPosLightSpace.xyz / fragPosLightSpace.w;
  projCoords         = projCoords * 0.5f + 0.5f;
  float closestDepth = unpack(texture2D(depthMap, projCoords.xy));
  float currentDepth = projCoords.z;

  float shadow = currentDepth - bias > closestDepth  ? 1.0f : 0.0f;
  //==if(projCoords.z > 1.0f)
  //  shadow = 0.0f;
  return shadow;
}

void main() {
  discardIfClipped(v_position);

  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  float shadow        = shadowCalculation(v_normal, u_mainLight.direction, v_positionLightTrans, u_shadowMap.nearDepthMap);
  vec4 lighting       = u_ambientLight + (1.0f - shadow) * v_lightDiffuse;

  vec4 diffuse        = lighting * texture;

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = lighting.rgb;
  #endif

  gl_FragColor = applyFog(diffuse, v_position);
}
