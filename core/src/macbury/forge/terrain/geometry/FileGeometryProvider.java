package macbury.forge.terrain.geometry;

import com.badlogic.gdx.utils.GdxRuntimeException;
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

    chunk.renderables.addAll(cache.renderables);
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
