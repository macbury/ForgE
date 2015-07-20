vec4 applyWind(float time, vec2 windDirection, float waviness, vec4 position, vec2 mapSize, sampler2D displacementTexture) {
  if (waviness >= 0.01f) {
    vec2 windUV  = vec2(position.x, position.z) / mapSize;
    vec4 texture = texture2D(displacementTexture, vec2(windUV.x + time * windDirection.x, windUV.y + time * windDirection.y));
    float hw     = waviness / 2.0f;
    position.x += hw - texture.r * waviness;
    position.z += hw - texture.g * waviness;
    return position;
  } else {
    return position;
  }
}

vec4 applyWind(float waviness, vec4 position) {
  return applyWind(u_time, u_windDirection, waviness, position, u_mapSize, u_windDisplacementTexture);
}
