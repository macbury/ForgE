varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying vec4   v_positionInLightSpace;

void main() {
  discardIfClipped(v_position);

  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  float bias          = shadowBias(v_normal, u_mainLight.direction);
  float shadow        = shadowCalculation(v_positionInLightSpace, u_shadowMap.farDepthMap, bias);
  //float shadow        = shadowCalculation(v_positionInLightSpace, u_shadowMap.nearDepthMap, bias);

  //vec4 positionInLightSpace = transformFrom0To1Range(u_shadowMap.farTransform * vec4(v_position.xyz, 1.0f));
  //float dof                 = texture2D(u_shadowMap.farDepthMap, positionInLightSpace.xy).r;
  //gl_FragColor              = vec4(texture2D(u_shadowMap.farDepthMap, positionInLightSpace.xy).rgb,1.0f);

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
