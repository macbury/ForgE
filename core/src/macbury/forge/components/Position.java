package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 19.10.14.
 */
public class Position extends Component implements Pool.Poolable, OctreeObject {
  public final BoundingBox box;
  public final Vector3     vector;
  public final Quaternion  rotation;
  public final Vector3     size;
  public final Vector3     scale;
  public OctreeNode        parent;
  public final Matrix4     worldTransform;
  public boolean visible = true;
  private final static Vector3 temp = new Vector3();

  public Position() {
    this.vector         = new Vector3();
    this.rotation       = new Quaternion();
    this.size           = new Vector3();
    this.scale          = new Vector3();
    this.box            = new BoundingBox();
    this.worldTransform = new Matrix4();
  }
  
  @Override
  public void reset() {
    vector.setZero();
    worldTransform.idt();
    rotation.set(Vector3.Z, 90);
    size.set(ChunkMap.TILE_SIZE);
    scale.set(1,1,1);
    parent = null;
    visible = false;
  }

  @Override
  public BoundingBox getBoundingBox() {
    return box.set(vector, temp.set(vector).add(size));
  }

  @Override
  public void setOctreeParent(OctreeNode parent) {
    this.parent = parent;
  }

  public Matrix4 getWorldTransformMatrix() {
    worldTransform.idt();
    worldTransform.setToTranslationAndScaling(vector, scale);
    worldTransform.rotate(rotation);
    return worldTransform;
  }
}
