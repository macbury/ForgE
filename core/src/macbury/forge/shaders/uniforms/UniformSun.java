package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformSun extends BaseUniform {
  private static final String DIRECTIONAL_LIGHT_TYPE = "DirectionalLight";
  public final String UNIFORM_AMBIENT_LIGHT          = "u_ambientLight";
  public final String UNIFORM_MAIN_LIGHT             = "u_mainLight";
  public final String UNIFORM_MAIN_LIGHT_COLOR       = "u_mainLight.color";
  public final String UNIFORM_MAIN_LIGHT_DIRECTION   = "u_mainLight.direction";

  @Override
  public void defineUniforms() {
    define(UNIFORM_MAIN_LIGHT, DIRECTIONAL_LIGHT_TYPE);
    define(UNIFORM_AMBIENT_LIGHT, Color.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_AMBIENT_LIGHT, env.ambientLight);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_COLOR, env.mainLight.color);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_DIRECTION, env.mainLight.direction);
  }

  @Override
  public void dispose() {

  }
}
