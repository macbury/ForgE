varying   vec2 v_texCoords;
void main() {
  vec4 depth                = texture2D(u_depthTexture, v_texCoords);
  vec4 sun                  = texture2D(u_sunTexture, v_texCoords);
  if (depth.a == 0.0f) {
    gl_FragColor = sun;
  } else {
    gl_FragColor = vec4(0.1f, 0.1f, 0.1f, 1.0f);
  }

}
