package macbury.forge.terrain.geometry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.ChunkPartCollider;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderableFactory;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 04.08.15.
 */
public class GeometryCache implements Disposable {
  public final Vector3i position  = new Vector3i();
  public Array<VoxelChunkRenderableFactory> factories = new Array<VoxelChunkRenderableFactory>();
  public Array<ChunkPartCollider> colliders           = new Array<ChunkPartCollider>();

  public GeometryCache(Chunk chunk) {
    this.position.set(chunk.position);
  }

  public GeometryCache() {

  }

  @Override
  public void dispose() {
    for (int i = 0; i < factories.size; i++) {
      factories.get(i).dispose();
    }

    for (int i = 0; i < colliders.size; i++) {
      colliders.get(i).dispose();
    }
    factories.clear();
    position.setZero();
    colliders.clear();

    factories = null;
    colliders = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (GeometryCache.class.isInstance(obj)) {
      GeometryCache otherObject = (GeometryCache) obj;
      return otherObject.position.equals(position);
    } else if (Chunk.class.isInstance(obj)) {
      Chunk otherObject = (Chunk) obj;
      return otherObject.position.equals(position);
    } else  {
      return false;
    }

  }
}
