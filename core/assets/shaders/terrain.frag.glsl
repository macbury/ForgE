uniform vec4      u_eyePosition;
uniform vec4      u_skyColor;
uniform sampler2D u_diffuseTexture;

varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying float  v_fogPower;

void main() {
  vec4 texture = texture2D(u_diffuseTexture, v_textCoord);
  vec4 diffuse = v_lightDiffuse * texture;

  #ifdef normalsDebugFlag
    diffuse.rgb = v_normal;
  #endif
  #ifdef lightingDebugFlag
    diffuse.rgb = v_lightDiffuse.rgb;
  #endif

  if (v_transparent >= 0.5f && texture.a <= 0.0f) {
    discard;
  }

  gl_FragColor = mix(fog(diffuse, u_skyColor, u_eyePosition, v_position), u_skyColor, v_fogPower);
}
