uniform sampler2D u_sateliteTexture;
varying   vec2 v_texCoord;

void main() {
  vec4 color   = texture2D(u_sateliteTexture, v_texCoord);
  gl_FragColor = color;
}
