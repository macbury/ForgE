package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 20.08.15.
 */
public class DepthShader extends RenderableBaseShader<Renderable> {

  private static final String UNIFORM_TRANSPARENT_DEPTH = "u_transparentDepthEnabled";

  @Override
  public boolean canRender(Renderable instance) {
    return false;
  }

  @Override
  public void beforeRender(Renderable renderable) {
    context.setDepthMask(true);
    context.setDepthTest(GL30.GL_LESS);
    if (env.depthShaderMode == LevelEnv.DepthShaderMode.Normal) {
      shader.setUniformf(UNIFORM_TRANSPARENT_DEPTH, 1.0f);
      if (BaseRenderable.haveTransparency(renderable.material)) {
        context.setCullFace(GL30.GL_NONE);
      } else {
        context.setCullFace(GL20.GL_FRONT);
      }
    } else {
      shader.setUniformf(UNIFORM_TRANSPARENT_DEPTH, 1.0f);
      context.setCullFace(GL20.GL_BACK);
    }

  }

  @Override
  public void afterBegin() {

  }
}
