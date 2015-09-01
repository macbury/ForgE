varying vec2 v_texCoords;

float fetchDepth(float x, float y) {
  return texture2D(u_depthTexture, v_texCoords + vec2(x*u_depthTexelSize, y*u_depthTexelSize)).x;
}

void main() {
  mat3 depthMatrix;
  depthMatrix[0][0] = fetchDepth(-1.0, -1.0);
  depthMatrix[1][0] = fetchDepth(0.0, -1.0);
  depthMatrix[2][0] = fetchDepth(1.0, -1.0);
  depthMatrix[0][1] = fetchDepth(-1.0, 0.0);
  depthMatrix[2][1] = fetchDepth(1.0, 0.0);
  depthMatrix[0][2] = fetchDepth(-1.0, 1.0);
  depthMatrix[1][2] = fetchDepth(0.0, 1.0);
  depthMatrix[2][2] = fetchDepth(1.0, 1.0);

  float gx = -1.0*depthMatrix[0][0]-2.0*depthMatrix[1][0]-1.0*depthMatrix[2][0]+1.0*depthMatrix[0][2]+2.0*depthMatrix[1][2]+1.0*depthMatrix[2][2];
  float gy = -1.0*depthMatrix[0][0]-2.0*depthMatrix[0][1]-1.0*depthMatrix[0][2]+1.0*depthMatrix[2][0]+2.0*depthMatrix[2][1]+1.0*depthMatrix[2][2];

  float result = sqrt(gx*gx + gy*gy);
  gl_FragColor = vec4(0.0f, 0.0f, 0.0f, result);
}
