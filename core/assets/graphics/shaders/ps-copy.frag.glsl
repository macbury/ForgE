uniform sampler2D u_srcTexture;
varying   vec2 v_texCoords;
void main() {
  gl_FragColor       = texture2D(u_srcTexture, v_texCoords);
}
