package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;

/**
 * Created by macbury on 29.04.15.
 */
public abstract class BaseRenderableUniform<T extends Renderable> extends BaseUniform {
  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {

  }

  public abstract void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera, T renderable);
}
