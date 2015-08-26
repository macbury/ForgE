varying   vec2 v_texCoords;
void main() {
  vec4 sum = vec4(0.0);

  float blurSize = 0.002f;
/*
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y - 4.0*blurSize)) * 0.05;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y - 3.0*blurSize)) * 0.09;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y - 2.0*blurSize)) * 0.12;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y - blurSize)) * 0.15;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y)) * 0.16;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y + blurSize)) * 0.15;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y + 2.0*blurSize)) * 0.12;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y + 3.0*blurSize)) * 0.09;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y + 4.0*blurSize)) * 0.05;

*/
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x - 4.0*blurSize, v_texCoords.y)) * 0.05;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x - 3.0*blurSize, v_texCoords.y)) * 0.1;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x - 2.0*blurSize, v_texCoords.y)) * 0.12;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x - blurSize, v_texCoords.y)) * 0.15;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x, v_texCoords.y)) * 0.16;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x + blurSize, v_texCoords.y)) * 0.15;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x + 2.0*blurSize, v_texCoords.y)) * 0.12;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x + 3.0*blurSize, v_texCoords.y)) * 0.1;
  sum += texture2D(u_downSampledMainTexture, vec2(v_texCoords.x + 4.0*blurSize, v_texCoords.y)) * 0.05;

  gl_FragColor = sum;
}
