varying vec3   v_normal;
varying vec4   v_position;
varying vec4   v_clipSpace;
varying vec3   v_cameraPosition;
varying vec2   v_texCoords;


void main() {
  float waveStrength       = 0.02f;
  vec2 disortionA          = (texture2D(u_waterRefractionDUDVMap, v_texCoords).rg * 2.0f - 1.0f) * waveStrength;
  vec2 ndc                 = (v_clipSpace.xy/v_clipSpace.w)/2.0f + 0.5f;
  vec2 refreactTexCords    = vec2(ndc.x, ndc.y);
  vec2 reflectionTexCords  = vec2(ndc.x, -ndc.y);

  refreactTexCords         += disortionA;
  refreactTexCords         = clamp(refreactTexCords, 0.001f, 0.999f);

  reflectionTexCords       += disortionA;
  reflectionTexCords.x     = clamp(reflectionTexCords.x, 0.001f, 0.999f);
  reflectionTexCords.y     = clamp(reflectionTexCords.y, -0.999f, -0.001f);

  vec4 refractionColor     = texture2D(u_refractionTexture, refreactTexCords);
  vec4 reflectionColor     = texture2D(u_reflectionTexture, reflectionTexCords);
  vec4 diffuse             = mix(reflectionColor, refractionColor, /*refractiveFactor(v_cameraPosition)*/ 0.5f);
  gl_FragColor             = applyFog(diffuse, v_position);
}
