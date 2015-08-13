varying vec3        v_texCoord;
varying vec4        v_position;
void main() {
  /*if (v_position.y <= 0.0f) {
    gl_FragColor      = u_skyColor;
  } else {
    gl_FragColor      = vec4(v_position.xyz, 1.0f);
  }*/
  vec4 color        = texture2D(u_skyMapTexture, vec2( u_skyMapProgress, clamp(1.0f - v_position.y, 0.0f, 1.0f) ));
  gl_FragColor      = vec4(color.rgb, 1.0f);
}
