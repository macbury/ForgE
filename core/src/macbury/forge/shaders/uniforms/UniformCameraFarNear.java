package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.08.15.
 */
public class UniformCameraFarNear extends BaseUniform {
  public static final String UNIFORM_FAR      = "u_cameraFar";
  public static final String UNIFORM_NEAR      = "u_cameraNear";
  @Override
  public void defineUniforms() {
    define(UNIFORM_FAR, Float.class);
    define(UNIFORM_NEAR, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_FAR, camera.far);
    shader.setUniformf(UNIFORM_NEAR, camera.near);
  }

  @Override
  public void dispose() {

  }
}
