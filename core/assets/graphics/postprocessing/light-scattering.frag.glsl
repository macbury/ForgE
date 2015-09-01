
varying   vec2 v_texCoords;

vec4 getSampleColor(vec2 coords) {
  return texture2D(u_mainTexture, coords);
}

void main() {
  vec4 finalColor            = vec4(0.0f, 0.0f, 0.0f, 1.0f);

  if (u_lightDirDotViewDir < 0.0f) {
    float lightDirDotViewDir = abs(u_lightDirDotViewDir);
    vec2 texCoord      = v_texCoords.st;
    vec2 deltaTexCoord = (1.0 / float(u_numSamples)) * u_density * vec2(texCoord.xy - u_lightPositonOnScreen.xy);
    float dist         = length(deltaTexCoord.xy);

    float threshold = 0.01f;
    if (dist > threshold) {
      deltaTexCoord.xy /= dist / threshold;
    }

    float illuminationDecay = 1.0f;
    for(int i=0; i < int(u_numSamples); i++) {
      texCoord -= deltaTexCoord;
      vec4 sampleColor = getSampleColor(texCoord);
      sampleColor *= illuminationDecay * u_weight;
      finalColor += sampleColor;
      illuminationDecay *= u_decay;
    }

    finalColor *= u_exposure * lightDirDotViewDir;
  }

  gl_FragColor               = finalColor;
}
