package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformEyePosition extends BaseUniform {
  public static final String UNIFORM_EYE_POSITION      = "u_eyePosition";
  private static final float CONST = 1.1881f;

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_EYE_POSITION, camera.position.x, camera.position.y, camera.position.z, CONST / (camera.far * camera.far));
  }

  @Override
  public void dispose() {

  }
}
