package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
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
    context.setDepthMask(true);
    if (BaseRenderable.haveTransparency(renderable.material)) {
      context.setCullFace(GL30.GL_NONE);
    } else {
      context.setCullFace(GL20.GL_FRONT);
    }
  }

  @Override
  public void afterBegin() {

  }
}
