

vec4 fog(vec4 inColot, vec4 fogColor, vec4 eyePosition, vec4 fragmentPosition) {
 vec3 flen = eyePosition.xyz - fragmentPosition.xyz;
 float fog = dot(flen, flen) * eyePosition.w;
 //if (fragmentPosition.y <= 3f) {
//   fog       = 1f;
 //} else {
   fog       = clamp(fog, 0.0, 1.0);
// }

 return mix(inColot, fogColor, fog);
}
