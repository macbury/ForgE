package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
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
  public Array<VoxelFaceRenderable> renderables = new Array<VoxelFaceRenderable>(6);
  public OctreeNode parent;
  private final static Vector3 temp = new Vector3();

  @Override
  public void getBoundingBox(BoundingBox outBox) {
    outBox.set(boundingBox);
  }

  public void updateBoundingBox() {
    boundingBox.set(worldPosition, temp.set(worldPosition).add(size));
    //boundingBox.min.scl(ChunkMap.TERRAIN_TILE_SIZE);
    //boundingBox.max.scl(ChunkMap.TERRAIN_TILE_SIZE);
  }

  @Override
  public void setOctreeParent(OctreeNode parent) {
    this.parent = parent;
  }

  @Override
  public void dispose() {
    clearFaces();
  }

  public void addFace(VoxelFaceRenderable face) {
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
}
