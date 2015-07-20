varying vec3   v_normal;
varying vec4   v_position;
varying vec4   v_clipSpace;

void main() {
  vec2 ndc                 = (v_clipSpace.xy/v_clipSpace.w)/2.0f + 0.5f;
  vec2 refreactTexCords    = vec2(ndc.x, ndc.y);
  vec2 reflectionTexCords  = vec2(ndc.x, ndc.y);

  vec4 refractionColor     = texture2D(u_refractionTexture, refreactTexCords);
  vec4 reflectionColor     = texture2D(u_reflectionTexture, reflectionTexCords);
  vec4 diffuse             = mix(reflectionColor, refractionColor, 0.5f);
  gl_FragColor             = applyFog(diffuse, v_position);
}
