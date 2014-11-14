float fogPowerByMapPosition(vec4 worldPosition, vec2 mapSize) {
  if (worldPosition.x <= 0.1f) {
    return 1.0f;
  } 

  if (worldPosition.x >= mapSize.x) {
    return 1.0f;
  }

  if (worldPosition.z <= 0.1f) {
    return 1.0f;
  } 

  if (worldPosition.z >= mapSize.x) {
    return 1.0f;
  }

  return 0f;
}

vec4 fog(vec4 inColot, vec4 fogColor, vec4 eyePosition, vec4 fragmentPosition) {
 vec3 flen = eyePosition.xyz - fragmentPosition.xyz;
 float fog = dot(flen, flen) * eyePosition.w;
 fog       = clamp(fog, 0.0, 1.0);
 return mix(inColot, fogColor, fog);
}
