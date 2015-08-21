package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.08.15.
 */
public class UniformShadowMap extends BaseUniform {
  private static final String STRUCT                            = "ShadowMap";
  private static final String UNIFORM_SHADOW_MAP                = "u_shadowMap";
  private static final String UNIFORM_SHADOW_FAR_MAP_TEXTURE    = "u_shadowMap.farDepthMap";
  private static final String UNIFORM_SHADOW_NEAR_MAP_TEXTURE   = "u_shadowMap.nearDepthMap";
  private static final String UNIFORM_SHADOW_MAP_FAR_TRANSFORM  = "u_shadowMap.farTransform";
  private static final String UNIFORM_SHADOW_MAP_NEAR_TRANSFORM = "u_shadowMap.nearTransform";
  private static final String UNIFORM_SHADOW_MAP_POSITION       = "u_shadowMap.lightPosition";
  @Override
  public void defineUniforms() {
    define(UNIFORM_SHADOW_MAP, STRUCT);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(UNIFORM_SHADOW_FAR_MAP_TEXTURE, context.textureBinder.bind(ForgE.fb.get(Fbo.FRAMEBUFFER_SUN_FAR_DEPTH).getColorBufferTexture()));
    shader.setUniformi(UNIFORM_SHADOW_NEAR_MAP_TEXTURE, context.textureBinder.bind(ForgE.fb.get(Fbo.FRAMEBUFFER_SUN_NEAR_DEPTH).getColorBufferTexture()));
    shader.setUniformMatrix(UNIFORM_SHADOW_MAP_FAR_TRANSFORM, env.mainLight.farMatrix);
    shader.setUniformMatrix(UNIFORM_SHADOW_MAP_NEAR_TRANSFORM, env.mainLight.nearMatrix);
    //shader.setUniformf(UNIFORM_SHADOW_MAP_POSITION, env.mainLight.getCamera().position);
  }

  @Override
  public void dispose() {

  }
}
