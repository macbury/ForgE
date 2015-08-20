varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying vec2   v_uvStart;
varying vec2   v_uvMul;

varying vec4   v_positionLightTrans;

void main() {
  discardIfClipped(v_position);
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);

  vec3 depth          = (v_positionLightTrans.xyz / v_positionLightTrans.w)*0.5f+0.5f;
  float len           = 1.0f - texture2D(u_shadowMap.depthMap, depth.xy).a;
  vec4 lighting       = (v_lightDiffuse - vec4(len, len, len, 0.0f));
  vec4 diffuse        = lighting * texture;

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = lighting.rgb;
  #endif

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = applyFog(diffuse, v_position);
}
