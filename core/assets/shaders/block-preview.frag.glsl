uniform sampler2D u_diffuseTexture;

varying vec3   v_normal;
varying vec2   v_textCoord;

void main() {
  vec4 texture = texture2D(u_diffuseTexture, v_textCoord);

  if (texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = texture;
}
