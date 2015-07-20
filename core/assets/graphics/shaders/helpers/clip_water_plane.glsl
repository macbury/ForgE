bool isOnPlane(vec3 point, vec3 normal, float elevation) {
  if (elevation <= 0.0f) {
    return false;
  } else if (normal.y <= 0.0f) {
    return point.y <= elevation;
  } else {
    return point.y >= elevation;
  }
}

bool isClipped(vec4 worldPosition) {
  return isOnPlane(worldPosition.xyz, u_clipWaterPlane.normal, u_clipWaterPlane.elevation);
}

void discardIfClipped(vec4 worldPosition) {
  if (isClipped(worldPosition)) {
    discard;
  }
}
