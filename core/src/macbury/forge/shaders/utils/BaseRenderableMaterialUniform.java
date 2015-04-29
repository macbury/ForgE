package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;

/**
 * Created by macbury on 29.04.15.
 */
public abstract class BaseRenderableMaterialUniform<T extends Attribute> extends BaseRenderableUniform {
  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera, Renderable renderable) {
    if (renderable.material.has(getAttributeType())) {
      bindAttribute(shader, context, (T)renderable.material.get(getAttributeType()));
    }
  }

  public abstract void bindAttribute(ShaderProgram shader, RenderContext context, T attribute);

  public abstract long getAttributeType();
}
