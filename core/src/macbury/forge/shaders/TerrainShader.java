package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix3;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.shaders.utils.CheckMaterial;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.10.14.
 */
public class TerrainShader extends RenderableBaseShader<VoxelChunkRenderable> {

  @Override
  public void afterBegin() {
    context.setDepthTest(GL20.GL_LEQUAL);
  }

  @Override
  public boolean canRender(Renderable instance) {
    return CheckMaterial.ifHaveSolidTerrainAttribute(instance.material) && VoxelChunkRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(VoxelChunkRenderable renderable) {
    if (renderable.haveTransparency()) {
      context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      context.setCullFace(GL30.GL_NONE);
    } else {
      context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      context.setCullFace(GL30.GL_BACK);
    }
  }

}
