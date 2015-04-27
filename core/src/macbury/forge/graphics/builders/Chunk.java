package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 19.10.14.
 */
public class Chunk implements OctreeObject, Disposable {
  private static final String TAG = "Chunk";
  /**
   * Position is position of chunk. To change it to world position it needs to be multiply by CHUNK_SIZE
   */
  public Vector3i position      = new Vector3i();

  /**
   * World position and size are world size and position
   */
  public Vector3    worldPosition = new Vector3();
  public Vector3    size          = new Vector3();
  /**
   * Start and end are offsets in array
   */
  public Vector3i start         = new Vector3i();
  public Vector3i end           = new Vector3i();
  public BoundingBox boundingBox  = new BoundingBox();
  public boolean needRebuild      = true;
  public final Array<VoxelChunkRenderable> renderables = new Array<VoxelChunkRenderable>(6);
  public final Array<ChunkPartCollider> colliders      = new Array<ChunkPartCollider>();
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
    //Gdx.app.log(TAG, "Disposing chunk!");
    clearFaces();
    clearColliders();
  }

  private void clearColliders() {
    while(colliders.size > 0) {
      colliders.pop().dispose();
    }
  }

  public void addFace(VoxelChunkRenderable face) {
    face.setParent(this);
    this.renderables.add(face);
  }

  public void clearFaces() {
    while(renderables.size > 0) {
      renderables.pop().dispose();
    }
  }

  /**
   * Check if chunk have any voxel faces
   * @return
   */
  public boolean isEmpty() {
    return renderables.size == 0;
  }

  public VoxelChunkRenderable getFace(int i) {
    return renderables.get(i);
  }
}
