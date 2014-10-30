

float fogNormal(vec4 eyePosition, vec4 fragmentPosition) {
 vec3 flen = eyePosition.xyz - fragmentPosition.xyz;
 float fog = dot(flen, flen) * eyePosition.w;
 return fog;
}
