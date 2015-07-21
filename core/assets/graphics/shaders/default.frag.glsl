varying vec3   v_normal;
varying vec4   v_lightDiffuse;
varying vec4   v_color;
varying vec4   v_position;
varying vec2   v_textCord;
void main() {
  discardIfClipped(v_position);
  vec4 texture        = texture2D(u_diffuseTexture, v_textCord);
  vec4 diffuse        = v_lightDiffuse * v_color * texture;
  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
}
