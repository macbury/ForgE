varying vec3   v_normal;
varying vec4   v_lightDiffuse;
varying vec4   v_color;
varying vec4   v_position;
varying vec2   v_textCord;
varying vec4   v_positionInLightSpace;

void main() {
  discardIfClipped(v_position);
  vec4 texture        = texture2D(u_diffuseTexture, v_textCord);

  float bias          = shadowBias(v_normal, u_mainLight.direction);
  float shadow        = shadowCalculation(v_positionInLightSpace, u_shadowMap.farDepthMap, bias);
  vec4 lighting       = u_ambientLight + (1.0f - shadow) * v_lightDiffuse;

  vec4 diffuse        = lighting * v_color * texture;

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  gl_FragColor = fog(diffuse, u_fogColor, u_eyePosition, v_position);
}
