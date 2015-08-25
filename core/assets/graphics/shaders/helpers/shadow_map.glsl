float calculateDepth(vec4 position, vec4 eyePosition, float cameraFar) {
  return position.z / cameraFar;//length(eyePosition.xyz - position.xyz) / cameraFar;
}

vec4 pack(float depth) {
  const HIGH vec4 bias = vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
  HIGH vec4 color = vec4(depth, fract(depth * 255.0), fract(depth * 65025.0), fract(depth * 160581375.0));
  return color - (color.yzww * bias);
}

float unpack(vec4 packedZValue) {
	const vec4 unpackFactors = vec4( 1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0 );
	return dot(packedZValue,unpackFactors);
}

float shadowBias(vec3 normal, vec3 lightDir) {
  return max(0.05f * (1.0f - dot(normal, lightDir)), 0.005f);
}

vec4 transformFrom0To1Range(vec4 vector) {
  return vector * 0.5f + 0.5f;
}

float shadowCalculation(vec4 positionInLightSpace, sampler2D depthMap, float bias) {
  // perform perspective divide
  vec3 projCoords         = positionInLightSpace.xyz / positionInLightSpace.w;
  // Transform to [0,1] range
  positionInLightSpace    = transformFrom0To1Range(positionInLightSpace);
  // Get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
  float closestDepth      = unpack(texture2D(depthMap, positionInLightSpace.xy));
  // Get depth of current fragment from light's perspective
  float currentDepth      = positionInLightSpace.z;
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
  float shadow            = currentDepth - bias < closestDepth  ? 0.0f : 1.0f;
  return shadow;
}
