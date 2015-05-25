#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

attribute vec4 a_position;

attribute vec4 a_material;
attribute vec2 a_texCoord0;
attribute vec4 a_textureFullCoords;

uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

uniform sampler2D u_windDisplacementTexture;
uniform vec2      u_windDirection;
uniform float     u_time;
uniform vec2      u_mapSize;

varying vec4   v_position;

varying vec2   v_textCoord;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying float  v_transparent;
varying float  v_depth;

void main() {
  float waviness    = a_material.a;
  v_transparent     = a_material.b;

  v_uvStart         = a_textureFullCoords.xy;
  v_uvMul           = a_textureFullCoords.zw - v_uvStart;
  v_textCoord       = a_texCoord0;

  v_position  = u_worldTransform * a_position;
  v_position  = applyWind(u_time, u_windDirection, waviness, v_position, u_mapSize, u_windDisplacementTexture);

  gl_Position = u_projectionMatrix *v_position;
}
