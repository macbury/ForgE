package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 21.08.15.
 */
public class UniformNearShadowDistance extends BaseUniform {
  public static final String UNIFORM_NEAR_SHADOW_DISTANCE      = "u_nearShadowDistance";
  private static final float SHADOW_OFFSET = 2;
  private final float distance;

  public UniformNearShadowDistance() {
    super();
    this.distance =  ForgE.config.nearShadowDistance + SHADOW_OFFSET;
  }

  @Override
  public void defineUniforms() {
    define(UNIFORM_NEAR_SHADOW_DISTANCE, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_NEAR_SHADOW_DISTANCE, distance);
  }

  @Override
  public void dispose() {

  }
}
