package macbury.forge.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 20.08.15.
 */
public class DepthShader extends RenderableBaseShader<Renderable> {
  @Override
  public boolean canRender(Renderable instance) {
    return false;
  }

  @Override
  public void beforeRender(Renderable renderable) {

  }

  @Override
  public void afterBegin() {

  }
}
