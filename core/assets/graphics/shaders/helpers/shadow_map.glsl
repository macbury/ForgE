float calculateDepth(vec4 position, vec4 eyePosition, float cameraFar) {
  return length(eyePosition.xyz - position.xyz) / cameraFar;
}

vec4 pack(float depth) {
  //const vec4 bias = vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
	//vec4 color      = vec4(depth, fract(depth * 255.0), fract(depth * 65025.0), fract(depth * 160581375.0));
//  return color - (color.yzww * bias);
  return vec4(depth, depth, depth, 1.0f);
}

float unpack(vec4 packedZValue) {
  //const vec4 bitShifts = vec4(1.0, 1.0 / 255.0, 1.0 / 65025.0, 1.0 / 160581375.0);
	//return dot(packedZValue,bitShifts);
  return packedZValue.r;
}

float shadowBias(vec3 normal, vec3 lightDir) {
  return max(0.05f * (1.0f - dot(normal, lightDir)), 0.005f);
}

vec4 transformFrom0To1Range(vec4 vector) {
  return vector * 0.5f + 0.5f;
}

float chebshevComputeQuantity(vec4 positionInLightSpace, sampler2D depthMap, float bias) {
  float currentDepth = (positionInLightSpace.z / positionInLightSpace.w )* 0.5f + 0.5f;
  vec2 projCoords    = (positionInLightSpace.xy / positionInLightSpace.w) * 0.5f + 0.5f;
  // get two moments
  vec2 moments      = texture2D(depthMap, projCoords.xy).rg;
  if (currentDepth <= moments.x) {
    return 1.0f;
  } else {
    float E_x2 = moments.y;
    float Ex_2 = moments.x * moments.x;
    float variance = E_x2 - (Ex_2);
    float t = currentDepth - moments.x;
    float pMax = variance / (variance + t*t);
    return pMax;
  }
}

float shadowCalculation(vec4 positionInLightSpace, sampler2D depthMap, float bias) {
  // perform perspective divide
  vec3 projCoords         = (positionInLightSpace.xyz / positionInLightSpace.w) * 0.5f + 0.5f;
  // Transform to [0,1] range
  //projCoords    = transformFrom0To1Range(positionInLightSpace);
  // Get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
  float closestDepth      = texture2D(depthMap, projCoords.xy).r;
  // Get depth of current fragment from light's perspective
  float currentDepth      = projCoords.z;
  // Check whether current frag pos is in shadow
/*
  vec2 texelSize          = vec2(0.0009765f);
  float shadow            = 0.0f;
  for(int x = -1; x <= 1; ++x) {
    for(int y = -1; y <= 1; ++y) {
      float pcfDepth = unpack(texture2D(depthMap, positionInLightSpace.xy + vec2(x, y) * texelSize));
      shadow += currentDepth - bias > pcfDepth ? 1.0f : 0.0f;
    }
  }
  shadow /= 9.0f;
*/
  float shadow            = currentDepth - bias < closestDepth  ? 1.0f : 0.0f;
  return shadow;
}
