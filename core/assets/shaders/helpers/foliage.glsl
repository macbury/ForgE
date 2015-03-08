vec4 applyWind(float time, float waviness, vec4 position, vec2 mapSize, sampler2D displacementTexture) {
  if (waviness >= 0.01f) {
    vec2 windUV  = vec2(position.x, position.z) / mapSize;
    vec4 texture = texture2D(displacementTexture, vec2(windUV.x + time * 0.1f, windUV.y + time* 0.1f));
    float hw     = waviness / 2f;
    position.x += hw - texture.r * waviness;
    position.z += hw - texture.g * waviness;
    return position;
  } else {
    return position;
  }
}
