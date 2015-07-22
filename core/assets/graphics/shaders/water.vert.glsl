attribute vec3 a_normal;
attribute vec4 a_position;

varying vec3   v_normal;
varying vec4   v_position;
varying vec4   v_clipSpace;
varying vec3   v_cameraPosition;
varying vec2   v_texCoords;
varying vec2   v_moveOffset;

void main() {
  v_moveOffset      = vec2(fract(u_waterSpeed * u_time),0.0f);
  v_normal          = normalize(u_normalMatrix * a_normal);
  v_position        = u_worldTransform * a_position;
  v_clipSpace       = u_projectionMatrix * v_position;
  v_cameraPosition  = u_eyePosition.xyz - v_position.xyz;
  v_texCoords       = vec2(a_position.x/2.0f+0.5f, a_position.z/2.0f + 0.5f) * u_waterDisplacementTiling;
  gl_Position       = v_clipSpace;
}
