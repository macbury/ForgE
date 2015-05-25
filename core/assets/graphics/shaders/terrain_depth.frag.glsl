#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision highp float;
#else
#define MED
#define LOWP
#define HIGH
#endif


uniform DirectionalLight u_mainLight;
varying vec4 v_position;

uniform sampler2D u_diffuseTexture;

varying vec2   v_textCoord;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying float  v_transparent;


vec4 getTiledTexel() {
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  return texture2D(u_diffuseTexture, tilingTextCord);
}


void main() {
  if (v_transparent >= 0.5f && getTiledTexel().a <= 0.0f) {
    discard;
  } else {
    //HIGH float depth = length(v_position.xyz-u_mainLight.position.xyz);
    //const HIGH vec4 bias = vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
    //HIGH vec4 color = vec4(depth, fract(depth * 255.0), fract(depth * 65025.0), fract(depth * 160581375.0));
    //gl_FragColor = color - (color.yzww * bias);
    gl_FragColor = vec4(length(v_position.xyz-u_mainLight.position.xyz) / u_mainLight.far);
  }

}
