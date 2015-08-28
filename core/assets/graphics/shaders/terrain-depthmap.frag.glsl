varying vec4   v_position;
varying vec2   v_uvStart;
varying vec2   v_uvMul;
varying vec2   v_textCoord;
varying float  v_depth;

void main() {
  vec2 tilingTextCord = (fract(v_textCoord) * v_uvMul) + v_uvStart;
  vec4 texture        = texture2D(u_diffuseTexture, tilingTextCord);
  if (texture.a <= 0.0f) {
    discard;
  }

  //homogeneus to texture cordinate system([-1,1])
/*
  float depth = v_position.z / v_position.w;
  depth       = depth * 0.5f + 0.5f;

  float M1    = depth; //moment 1
  float M2    = depth * depth;

	float dx    = dFdx(depth);
	float dy    = dFdy(depth);

  M2 += 0.25f*(dx*dx+dy*dy);

  gl_FragColor = vec4(M1, M2, 0.0f, 1.0f);*/
  /*float depth = v_depth * 0.5f + 0.5f;;
  gl_FragColor = vec4(depth);
  gl_FragColor.a = 1.0f;*/

  gl_FragColor    = pack(v_depth);

}
