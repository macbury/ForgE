uniform sampler2D        u_sunDepthMap;
uniform DirectionalLight u_mainLight;
varying vec2             v_texCoords;
void main() {
  gl_FragColor = texture2D(u_sunDepthMap, v_texCoords);
}
