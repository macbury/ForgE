attribute vec4 a_position;
attribute vec4 a_material;
attribute vec4 a_textureFullCoords;
attribute vec2 a_texCoord0;

varying vec2   v_textCoord;
varying vec4   v_position;
varying vec2   v_uvStart;
varying vec2   v_uvMul;

void main() {
  float waviness    = a_material.a;
  v_textCoord       = a_texCoord0;
  v_uvStart         = a_textureFullCoords.xy;
  v_uvMul           = a_textureFullCoords.zw - v_uvStart;

  v_position        = u_worldTransform * vec4(a_position.xyz, 1.0f);
  v_position        = applyWind(waviness, v_position);
  gl_Position       = u_projectionMatrix * v_position;
}
