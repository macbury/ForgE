package macbury.forge.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.graphics.batch.renderable.SkyboxRenderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.05.15.
 */
public class SkyboxShader extends RenderableBaseShader<SkyboxRenderable> {
  @Override
  public boolean canRender(Renderable instance) {
    return SkyboxRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(SkyboxRenderable renderable) {

  }

  @Override
  public void afterBegin() {

  }
}
