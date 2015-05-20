package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.shaders.utils.DepthRenderableBaseShader;
import macbury.forge.storage.serializers.graphics.VoxelFaceRenderableSerializer;

/**
 * Created by macbury on 20.05.15.
 */
public class TerrainDepthShader extends DepthRenderableBaseShader<Renderable> {
  @Override
  public boolean canRender(Renderable instance) {
    return VoxelChunkRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(Renderable renderable) {
    context.setCullFace(GL30.GL_BACK);
  }

  @Override
  public void afterBegin() {

  }
}
