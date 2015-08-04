package macbury.forge.terrain.geometry;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.voxel.ChunkMap;

import java.util.HashMap;

/**
 * Created by macbury on 04.08.15.
 */
public class DynamicGeometryProvider extends TerrainGeometryProvider {
  private TerrainBuilder builder;

  public DynamicGeometryProvider(ChunkMap map) {
    this.builder = new TerrainBuilder(map);
  }

  @Override
  public void build(Chunk chunk) {
    GeometryCache geometryCache = new GeometryCache(chunk);
    int cacheIndex              = caches.indexOf(geometryCache, false);
    if (cacheIndex != -1) {
      caches.get(cacheIndex).dispose();
      caches.removeIndex(cacheIndex);
    }

    caches.add(geometryCache);

    builder.begin(); {
      builder.cursor.set(chunk);

      while(builder.next()) {
        builder.buildFaceForChunk(chunk);
      }
      builder.assembleMesh(chunk);
    } builder.end();

    geometryCache.renderables.addAll(chunk.renderables);
  }

  @Override
  public void dispose() {
    super.dispose();

    builder = null;
  }
}
