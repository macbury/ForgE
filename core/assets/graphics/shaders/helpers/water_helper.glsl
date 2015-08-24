float refractiveFactor(vec3 cameraPosition, float factor, vec3 normal) {
  vec3 viewVector = normalize(cameraPosition);
  return pow(dot(viewVector, normal), factor);
}
