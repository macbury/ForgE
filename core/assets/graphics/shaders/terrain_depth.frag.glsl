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


uniform DirectionalLight u_mainLight;
varying vec4 v_position;

void main() {
  gl_FragColor = vec4(length(v_position.xyz-u_mainLight.position)/u_mainLight.far);
}
