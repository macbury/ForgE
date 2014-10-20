package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.10.14.
 */
public class TerrainShader extends RenderableBaseShader<TerrainChunkRenderable> {
  @Override
  public boolean canRender(BaseRenderable instance) {
    return TerrainChunkRenderable.class.isInstance(instance);
  }

  @Override
  public void afterBegin() {
    context.setCullFace(GL30.GL_BACK);
    context.setDepthTest(GL20.GL_LEQUAL);
  }

  @Override
  public void beforeRender(TerrainChunkRenderable renderable) {

  }
}
