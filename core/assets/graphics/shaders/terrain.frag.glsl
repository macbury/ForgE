
varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying vec2   v_uvStart;
varying vec2   v_uvMul;

void main() {
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  vec4 diffuse        = v_lightDiffuse * texture;

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
}
