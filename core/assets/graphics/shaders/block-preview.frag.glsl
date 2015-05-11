uniform sampler2D u_diffuseTexture;

varying vec3   v_normal;
varying vec2   v_textCoord;
varying vec2   v_uvStart;
varying vec2   v_uvMul;

void main() {
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  if (texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = texture;
}
