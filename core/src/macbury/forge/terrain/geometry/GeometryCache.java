package macbury.forge.terrain.geometry;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 04.08.15.
 */
public class GeometryCache implements Disposable {
  public final Vector3i position  = new Vector3i();
  public final Array<VoxelChunkRenderable> renderables = new Array<VoxelChunkRenderable>();

  public GeometryCache(Chunk chunk) {
    this.position.set(chunk.position);
  }

  public GeometryCache() {

  }

  @Override
  public void dispose() {
    renderables.clear();
    position.setZero();
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
