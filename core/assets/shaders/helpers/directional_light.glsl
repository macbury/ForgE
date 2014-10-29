struct DirectionalLight {
    vec3 direction;
    vec4 color;
};
vec3 directionalLightDiffuse(DirectionalLight source, vec3 normal) {
    vec3 lightDir = -source.direction;
    float NdotL = clamp(dot(normal, lightDir), 0.0f, 1.0f);
    vec3 value = source.color.rgb * NdotL;
    return value;
}
/*
vec3 sunLight(vec3 surfaceNormal, vec3 eyeNormal, float shiny, float spec, float diffuse){
    vec3 diffuseColor = max(dot(sunDirection, surfaceNormal),0.0)*sunColor*diffuse;
    vec3 reflection = normalize(reflect(-sunDirection, surfaceNormal));
    float direction = max(0.0, dot(eyeNormal, reflection));
    vec3 specular = pow(direction, shiny)*sunColor*spec;
    return diffuseColor + specular;
}*/