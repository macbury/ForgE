varying vec3   v_normal;
varying vec4   v_lightDiffuse;
varying vec4   v_color;
void main() {
  vec4 diffuse        = v_lightDiffuse * v_color;
  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif
  gl_FragColor = diffuse;
}