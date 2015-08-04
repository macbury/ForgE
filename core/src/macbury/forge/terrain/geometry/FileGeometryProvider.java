package macbury.forge.terrain.geometry;

import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 04.08.15.
 */
public class FileGeometryProvider extends TerrainGeometryProvider {

  @Override
  public void build(Chunk chunk) {
    GeometryCache cache = findForChunk(chunk);
    if (cache == null) {
      throw new GdxRuntimeException("Could not find cache for chunk: " + chunk.position.toString());
    }

    for (VoxelChunkRenderable renderable : cache.renderables) {
      chunk.renderables.add(renderable);
      renderable.setParent(chunk);
    }
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
