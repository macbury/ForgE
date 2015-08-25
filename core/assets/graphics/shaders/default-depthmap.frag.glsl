varying vec4   v_position;

void main() {
  float depth     = calculateDepth(v_position, u_eyePosition, u_cameraFar);
  gl_FragColor    = pack(depth);
}
