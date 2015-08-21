struct ShadowMap {
  sampler2D farDepthMap;
  sampler2D nearDepthMap;
  mat4 farTransform;
  mat4 nearTransform;
};
