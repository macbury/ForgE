package macbury.forge.graphics.batch.renderable;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.builders.Chunk;

/**
 * Created by macbury on 23.10.14.
 */
public class VoxelFaceRenderable extends BaseRenderable implements Disposable {
  public Vector3 direction = new Vector3();
  private Chunk parent;
  public BoundingBox boundingBox = new BoundingBox();


  @Override
  public void dispose() {
    if (mesh != null) {
      mesh.dispose();
    }
    parent = null;
    mesh = null;
  }

  public void setParent(Chunk parent) {
    this.parent = parent;
  }

  public Chunk getParent() {
    return parent;
  }
}
