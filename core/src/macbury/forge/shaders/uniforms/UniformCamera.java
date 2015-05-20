package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.05.15.
 */
public class UniformCamera extends BaseUniform {
  public final String UNIFORM_CAMERA_POSITION   = "u_camera.position";
  public final String UNIFORM_CAMERA_FAR        = "u_camera.far";
  public final String UNIFORM_CAMERA_NEAR       = "u_camera.near";
  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_CAMERA_NEAR, camera.near);
    shader.setUniformf(UNIFORM_CAMERA_FAR, camera.far);
    shader.setUniformf(UNIFORM_CAMERA_POSITION, camera.position);
  }

  @Override
  public void dispose() {

  }
}
