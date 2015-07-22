vec3 colorToNormal(vec4 color, bool yInvert) {
  color.g = yInvert ? 1.0 - color.g : color.g;
  vec3 normal = normalize(color.rgb * 2.0 - 1.0);
  return normal;
}

vec3 extractNormal(sampler2D normal_texture, vec2 pos) {
  return normalize(texture2D(normal_texture, pos).rgb * 2.0 - 1.0);
}
