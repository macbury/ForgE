uniform sampler2D u_sateliteTexture;
varying   vec2 v_texCoord;
varying   vec4 v_position;
void main() {
  //discardIfClipped(v_position);
  gl_FragColor      = texture2D(u_sateliteTexture, v_texCoord);
}
