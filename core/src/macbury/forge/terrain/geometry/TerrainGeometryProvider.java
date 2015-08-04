package macbury.forge.terrain.geometry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.builders.Chunk;

/**
 * Created by macbury on 04.08.15.
 */
public abstract class TerrainGeometryProvider implements Disposable {
  public final Array<GeometryCache> caches = new Array<GeometryCache>();
  /*
  Applies geometry to chunk
   */
  public abstract void build(Chunk chunk);

  protected GeometryCache findForChunk(Chunk chunk) {
    for (int i = 0; i < caches.size; i++) {
      GeometryCache cache = caches.get(i);
      if (cache.equals(chunk)) {
        return cache;
      }
    }
    return null;
  }

  @Override
  public void dispose() {
    for (GeometryCache cache : caches) {
      cache.dispose();
    }
    caches.clear();
  }
}
