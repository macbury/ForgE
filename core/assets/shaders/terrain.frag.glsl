varying vec4   v_color;
varying vec3   v_normal;
varying vec3   v_lightDiffuse;
void main() {
  vec4 final   = v_color * vec4(v_lightDiffuse.rgb, 0.1f);

  gl_FragColor = final;
}
