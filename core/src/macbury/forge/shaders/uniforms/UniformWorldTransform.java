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
public class UniformWorldTransform extends BaseRenderableUniform {
  public static final String UNIFORM_WORLD_TRANSFORM = "u_worldTransform";

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera, Renderable renderable) {
    shader.setUniformMatrix(UNIFORM_WORLD_TRANSFORM, renderable.worldTransform);
  }

  @Override
  public void dispose() {

  }
}
