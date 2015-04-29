uniform vec4   u_eyePosition;
uniform vec4   u_skyColor;

varying vec3   v_normal;
varying vec4   v_lightDiffuse;
varying vec4   v_color;
varying vec4   v_position;

void main() {
  vec4 diffuse        = v_lightDiffuse * v_color;
  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
}