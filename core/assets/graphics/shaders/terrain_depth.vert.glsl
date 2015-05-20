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

uniform mat4   u_projectionMatrix;
uniform mat4   u_worldTransform;

varying vec4   v_position;

void main() {
  v_position  = u_worldTransform * a_position;
  gl_Position = u_projectionMatrix *v_position;
}
