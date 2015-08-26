varying   vec2 v_texCoords;
void main() {
  vec4 mainColor     = texture2D(u_mainColorTexture, v_texCoords);
  vec4 vignetteColor = texture2D(u_vignetteTexture, v_texCoords);
  vec4 bloomColor    = texture2D(u_bloomTexture, v_texCoords);
  gl_FragColor       = mix(mainColor, bloomColor, 0.25f) * vignetteColor;
}
