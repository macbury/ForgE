package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformSun extends BaseUniform {
  public final String UNIFORM_AMBIENT_LIGHT          = "u_ambientLight";
  public final String UNIFORM_MAIN_LIGHT_COLOR       = "u_mainLight.color";
  public final String UNIFORM_MAIN_LIGHT_DIRECTION   = "u_mainLight.direction";
  public final String UNIFORM_MAIN_LIGHT_POSITION    = "u_mainLight.position";
  public final String UNIFORM_MAIN_LIGHT_FAR         = "u_mainLight.far";
  public final String UNIFORM_MAIN_LIGHT_TRANS_MAT   = "u_mainLight.transMatrix";
  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_AMBIENT_LIGHT, env.ambientLight);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_COLOR, env.mainLight.color);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_DIRECTION, env.mainLight.direction);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_POSITION, env.mainLight.getCamera().position);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_FAR, env.mainLight.getCamera().far);
    shader.setUniformMatrix(UNIFORM_MAIN_LIGHT_TRANS_MAT, env.mainLight.getCamera().combined);
  }

  @Override
  public void dispose() {

  }
}
