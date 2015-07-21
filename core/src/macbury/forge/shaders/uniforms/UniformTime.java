package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformTime extends BaseUniform {
  public final String UNIFORM_TIME            = "u_time";

  @Override
  public void defineUniforms() {
    define(UNIFORM_TIME, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_TIME, ForgE.graphics.getElapsedTime());
  }

  @Override
  public void dispose() {

  }
}
