varying   vec2 v_texCoords;
void main() {
  vec4 mainColor     = texture2D(u_mainTexture, v_texCoords);
  vec4 vignetteColor = texture2D(u_vignetteTexture, v_texCoords);
  vec4 blurColor     = texture2D(u_blurTexture, v_texCoords);
  gl_FragColor       = mix(mainColor, blurColor, u_blurMix) * vignetteColor;
}
