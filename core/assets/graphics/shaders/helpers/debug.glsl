const float DEBUG_NORMAL_RENDERING = 0f;
const float DEBUG_NORMAL_NORMALS   = 2f;
const float DEBUG_NORMAL_LIGHTING  = 3f;
uniform float u_debugMode;

bool shouldDebugRenderNormals() {
  return u_debugMode == DEBUG_NORMAL_NORMALS;
}

bool shouldDebugRenderLighting() {
  return u_debugMode == DEBUG_NORMAL_LIGHTING;
}

