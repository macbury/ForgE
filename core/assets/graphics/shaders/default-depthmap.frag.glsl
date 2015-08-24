varying vec4   v_position;

void main() {
  float depth     = length(v_position.xyz-u_eyePosition.xyz) / u_cameraFar;
  gl_FragColor    = pack(depth);
}
