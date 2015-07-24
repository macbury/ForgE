vec3 directionalLightDiffuse(DirectionalLight source, vec3 normal) {
    vec3 lightDir = -source.direction;
    float NdotL   = clamp(dot(normal, lightDir), 0.0f, 1.0f);
    return source.color.rgb * NdotL;
}

vec3 directionalLightSpecular(DirectionalLight source, vec3 normal, float shineDamper, float reflectivity, vec3 eyePosition) {
  vec3 lightDir          = -source.direction;
  vec3 reflectDir        = reflect(lightDir, normal);
  float specularFactor   = clamp(dot(reflectDir, eyePosition), 0.0f, 1.0f);
  float dampedFactor     = pow(specularFactor, shineDamper);
  return source.color.rgb  * dampedFactor * reflectivity;
}

vec3 applySunLight(vec3 normal) {
  return directionalLightDiffuse(u_mainLight, normal);
}

vec3 sunLight(DirectionalLight source, vec3 surfaceNormal, vec3 eyeNormal, float shiny, float spec, float diffuse){
    vec3 diffuseColor = max(dot(source.direction, surfaceNormal),0.0)*source.color.rgb*diffuse;
    vec3 reflection = normalize(reflect(-source.direction, surfaceNormal));
    float direction = max(0.0, dot(eyeNormal, reflection));
    vec3 specular = pow(direction, shiny)*source.color.rgb*spec;
    return diffuseColor + specular;
}
