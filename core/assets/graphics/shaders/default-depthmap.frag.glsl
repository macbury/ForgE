varying vec4   v_position;

void main() {
  gl_FragColor    = vec4(length(v_position.xyz-u_eyePosition.xyz)/u_cameraFar);
}
