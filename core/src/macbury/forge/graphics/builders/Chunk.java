package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 19.10.14.
 */
public class Chunk implements OctreeObject, Disposable {
  public Vector3Int position      = new Vector3Int();
  public Vector3    worldPosition = new Vector3();
  public Vector3    size          = new Vector3();
  public Vector3Int start         = new Vector3Int();
  public Vector3Int end           = new Vector3Int();
  public BoundingBox boundingBox  = new BoundingBox();
  public boolean needRebuild      = true;
  public TerrainChunkRenderable   renderable;
  public OctreeNode parent;
  private final static Vector3 temp = new Vector3();

  @Override
  public void getBoundingBox(BoundingBox outBox) {
    outBox.set(boundingBox);
  }

  public void updateBoundingBox() {
    boundingBox.set(worldPosition, temp.set(worldPosition).add(size));
  }

  @Override
  public void setOctreeParent(OctreeNode parent) {
    this.parent = parent;
  }

  @Override
  public void dispose() {
    if (renderable != null) {
      if (renderable.mesh != null) {
        renderable.mesh.dispose();
        renderable.mesh = null;
      }
    }
  }

}
