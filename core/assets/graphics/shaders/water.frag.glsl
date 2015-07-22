varying vec3   v_normal;
varying vec4   v_position;
varying vec4   v_clipSpace;
varying vec3   v_cameraPosition;
varying vec2   v_texCoords;
varying vec2   v_moveOffset;


void main() {
  vec2 disortionA          = (texture2D(u_waterRefractionDUDVMap, v_texCoords + v_moveOffset).rg * 2.0f - 1.0f) * u_waterWaveStrength;
  vec2 disortionB          = (texture2D(u_waterRefractionDUDVMap, vec2(-v_texCoords.x, v_texCoords.y) + v_moveOffset).rg * 2.0f - 1.0f) * u_waterWaveStrength;

  vec2 totalDisortion      = disortionA + disortionB;

  vec2 ndc                 = (v_clipSpace.xy/v_clipSpace.w)/2.0f + 0.5f;
  vec2 refreactTexCords    = vec2(ndc.x, ndc.y);
  vec2 reflectionTexCords  = vec2(ndc.x, -ndc.y);

  refreactTexCords         += totalDisortion;
  refreactTexCords         = clamp(refreactTexCords, 0.001f, 0.999f);

  reflectionTexCords       += totalDisortion;
  reflectionTexCords.x     = clamp(reflectionTexCords.x, 0.001f, 0.999f);
  reflectionTexCords.y     = clamp(reflectionTexCords.y, -0.999f, -0.001f);

  vec4 refractionColor     = texture2D(u_refractionTexture, refreactTexCords);
  vec4 reflectionColor     = texture2D(u_reflectionTexture, reflectionTexCords);
  vec4 waterDiffuse        = mix(reflectionColor, refractionColor, refractiveFactor(v_cameraPosition, u_waterRefractionFactor));
  vec4 waterColor          = mix(waterDiffuse, u_waterColor, u_waterColorTint);

  gl_FragColor             = applyFog(waterColor, v_position);
}
