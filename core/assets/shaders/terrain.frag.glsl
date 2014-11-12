uniform vec4      u_eyePosition;
uniform vec4      u_skyColor;
uniform sampler2D u_diffuseTexture;

varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;

void main() {
  vec4 diffuse = v_lightDiffuse * texture2D(u_diffuseTexture, v_textCoord);

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  gl_FragColor = fog(diffuse, u_skyColor, u_eyePosition, v_position);
}
