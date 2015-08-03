varying vec3   v_normal;
varying vec4   v_position;
varying vec4   v_clipSpace;
varying vec3   v_cameraPosition;
varying vec2   v_texDisplacementCoords;
varying vec2   v_texNormalCoords;
varying vec2   v_moveOffset;

void main() {
  vec2 distortedTexCoords = texture2D(u_waterRefractionDUDVMap, v_texDisplacementCoords + v_moveOffset).rg*0.1f;
  distortedTexCoords      = v_texDisplacementCoords + vec2(distortedTexCoords.x, distortedTexCoords.y+v_moveOffset.x);
  vec2 totalDisortion     = (texture2D(u_waterRefractionDUDVMap, distortedTexCoords).rg * 2.0f - 1.0f) * u_waterWaveStrength;

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

  vec4 multiSampledNormalTextureValue = max(
    texture2D(u_waterNormalATexture, distortedTexCoords),
    texture2D(u_waterNormalBTexture, distortedTexCoords)
  );

  vec3 normal              = normalize(vec3(
    multiSampledNormalTextureValue.r * 2.0f - 1.0f,
    multiSampledNormalTextureValue.b,
    multiSampledNormalTextureValue.g * 2.0f - 1.0f
  ));

  vec4 specularComponent   = vec4(directionalLightSpecular(u_mainLight, normal, u_waterShineDamper, u_waterReflectivity, normalize(v_cameraPosition)), 0.0f);

  vec4 lightColor          = vec4(u_ambientLight.rgb + applySunLight(normal), 1.0f);

  vec4 finalColor          = waterColor + specularComponent;

  #ifdef normalsDebugFlag
    finalColor.rgb = normal;
  #endif
  #ifdef lightingDebugFlag
    finalColor.rgb = lightColor.rgb + specularComponent.rgb;
  #endif

  gl_FragColor             = applyFog(finalColor, v_position);
}
