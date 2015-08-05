package macbury.forge.terrain.geometry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.renderable.VoxelChunkRenderableFactory;
import macbury.forge.voxel.ChunkMap;

import java.util.HashMap;

/**
 * Created by macbury on 04.08.15.
 */
public class DynamicGeometryProvider extends TerrainGeometryProvider {
  private TerrainBuilder builder;
  private Array<VoxelChunkRenderableFactory> tempOut = new Array<VoxelChunkRenderableFactory>();
  private final static String TAG = "DynamicGeometryProvider";

  public DynamicGeometryProvider(ChunkMap map) {
    this.builder = new TerrainBuilder(map);
  }

  @Override
  public void build(Chunk chunk) {
    tempOut.clear();
    GeometryCache geometryCache = new GeometryCache(chunk);
    int cacheIndex              = caches.indexOf(geometryCache, false);

    if (cacheIndex != -1) {
      //Gdx.app.log(TAG, "Replacing chunk: " + chunk.toString());
      caches.get(cacheIndex).dispose();
      caches.removeIndex(cacheIndex);
    } else {
      //Gdx.app.log(TAG, "Adding chunk: " + chunk.toString());
    }

    builder.begin(); {
      builder.cursor.set(chunk);

      while(builder.next()) {
        builder.buildFaceForChunk(chunk);
      }
      builder.assembleFactories(chunk, tempOut);
    } builder.end();


    for (int i = 0; i < tempOut.size; i++) {
      VoxelChunkRenderableFactory factory = tempOut.get(i);
      VoxelChunkRenderable renderable     = factory.get();
      geometryCache.factories.add(factory);
      chunk.addFace(renderable);
    }
    caches.add(geometryCache);
    geometryCache.colliders.addAll(chunk.colliders);
    tempOut.clear();
  }

  @Override
  public void dispose() {
    super.dispose();
    tempOut.clear();
    builder = null;
  }
}
