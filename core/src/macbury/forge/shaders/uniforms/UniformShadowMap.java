package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.08.15.
 */
public class UniformShadowMap extends BaseUniform {
  private static final String STRUCT             = "ShadowMap";
  private static final String UNIFORM_SHADOW_MAP = "u_shadowMap";
  private static final String UNIFORM_SHADOW_MAP_TEXTURE = "u_shadowMap.depthMap";
  private static final String UNIFORM_SHADOW_MAP_TRANSFORM  = "u_shadowMap.lightTransform";
  private static final String UNIFORM_SHADOW_MAP_POSITION  = "u_shadowMap.lightPosition";
  @Override
  public void defineUniforms() {
    define(UNIFORM_SHADOW_MAP, STRUCT);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(UNIFORM_SHADOW_MAP_TEXTURE, context.textureBinder.bind(ForgE.fb.get(Fbo.FRAMEBUFFER_SUN_DEPTH).getColorBufferTexture()));
    shader.setUniformMatrix(UNIFORM_SHADOW_MAP_TRANSFORM, env.mainLight.getCamera().combined);
    shader.setUniformf(UNIFORM_SHADOW_MAP_POSITION, env.mainLight.getCamera().position);
  }

  @Override
  public void dispose() {

  }
}
