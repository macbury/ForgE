package macbury.forge.octree.query;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 29.10.14.
 */
public class FrustrumOctreeQuery extends OctreeQuery {
  private Frustum frustum;
  private BoundingBox tempBox = new BoundingBox();

  @Override
  public boolean checkNode(OctreeNode node) {
    return frustum.boundsInFrustum(node.getBounds());
  }

  @Override
  public boolean checkObject(OctreeObject object) {
    object.getBoundingBox(tempBox);
    return (frustum.boundsInFrustum(tempBox));
  }

  public Frustum getFrustum() {
    return frustum;
  }

  public void setFrustum(Frustum frustum) {
    this.frustum = frustum;
  }
}
