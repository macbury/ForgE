vec4 pack(float depth) {
  const HIGH vec4 bias = vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
  HIGH vec4 color = vec4(depth, fract(depth * 255.0), fract(depth * 65025.0), fract(depth * 160581375.0));
  return color - (color.yzww * bias);
}

float unpack(vec4 packedZValue) {
	const vec4 unpackFactors = vec4( 1.0 / (256.0 * 256.0 * 256.0), 1.0 / (256.0 * 256.0), 1.0 / 256.0, 1.0 );
	return dot(packedZValue,unpackFactors);
}
