vec3 colorToNormal(vec4 color, bool yInvert) {
  color.g = yInvert ? 1.0 - color.g : color.g;
  vec3 normal = normalize(color.rgb * 2.0 - 1.0);
  return normal;
}