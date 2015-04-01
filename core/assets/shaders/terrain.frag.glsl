uniform vec4      u_eyePosition;
uniform vec4      u_skyColor;
uniform sampler2D u_diffuseTexture;

varying vec4   v_lightDiffuse;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_textCoord;
varying float  v_transparent;
varying float  v_fogPower;
varying vec2   v_textureTiling;
void main() {
  vec2 tiledTexCord = mod(v_textCoord, v_textureTiling);
  vec4 texture = texture2D(u_diffuseTexture, tiledTexCord);
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
  diffuse.r *= v_textureTiling.x / 10f;
  diffuse.g *= v_textureTiling.y / 10f;
 // gl_FragColor = mix(fog(diffuse, u_skyColor, u_eyePosition, v_position), u_skyColor, v_fogPower);
  gl_FragColor = diffuse;
}
