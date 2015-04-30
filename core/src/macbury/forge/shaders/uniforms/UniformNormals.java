package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseRenderableUniform;

/**
 * Created by macbury on 29.04.15.
 */
public class UniformNormals extends BaseRenderableUniform {
  public final String UNIFORM_NORMAL_MATRIX   = "u_normalMatrix";

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera, Renderable renderable) {
    //tempMatrix.set(renderable.worldTransform).inv().transpose();
    shader.setUniformMatrix(UNIFORM_NORMAL_MATRIX, tempMatrix);
  }

  @Override
  public void dispose() {

  }
}
