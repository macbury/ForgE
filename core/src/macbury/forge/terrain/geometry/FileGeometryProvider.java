package macbury.forge.terrain.geometry;

import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.ChunkPartCollider;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderableFactory;
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

    for (VoxelChunkRenderableFactory factory : cache.factories) {
      VoxelChunkRenderable renderable = factory.get();
      chunk.renderables.add(renderable);
      renderable.setParent(chunk);
      //factory.dispose();
    }

    for (ChunkPartCollider collider : cache.colliders) {
      collider.assemble(ChunkMap.TERRAIN_TILE_SIZE);
      chunk.colliders.add(collider);
    }
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
