uniform sampler2D u_sunDepthMap;
varying vec2      v_texCoords;
void main() {
  gl_FragColor = texture2D(u_sunDepthMap, v_texCoords) + vec4(1,0,0,0);
}
