varying vec4   v_position;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying vec2   v_textCoord;

void main() {
  float depth          = length(v_position.xyz-u_eyePosition.xyz) / u_cameraFar;

//  if (u_cameraNear <= u_nearShadowDistance) {
    vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
    vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);
    if (texture.a <= 0.0f) {
      discard;
    }
//  }

  gl_FragColor       = pack(depth);
}
