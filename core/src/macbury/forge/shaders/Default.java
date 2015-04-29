package macbury.forge.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.graphics.batch.renderable.SpriteRenderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 29.04.15.
 */
public class Default  extends RenderableBaseShader<Renderable> {
  @Override
  public boolean canRender(Renderable instance) {
    return true;
  }

  @Override
  public void beforeRender(Renderable renderable) {

  }

  @Override
  public void afterBegin() {

  }
}
