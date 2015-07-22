float refractiveFactor(vec3 cameraPosition, float factor) {
  vec3 viewVector = normalize(cameraPosition);
  return pow(dot(viewVector, vec3(0.0f, 1.0f, 0.0f)), factor);
}
