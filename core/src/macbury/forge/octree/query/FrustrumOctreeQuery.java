package macbury.forge.octree.query;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 29.10.14.
 */
public class FrustrumOctreeQuery extends OctreeQuery {
  private Frustum frustum;
  private BoundingBox tempBox = new BoundingBox();
  private Vector3 tmpV = new Vector3();

  @Override
  public boolean checkNode(OctreeNode node) {
    return frustum.boundsInFrustum(node.getBounds());
  }

  @Override
  public boolean checkObject(OctreeObject object) {
    object.getBoundingBox(tempBox);
    for (int i = 0, len2 = frustum.planes.length; i < len2; i++) {
      if (frustum.planes[i].testPoint(tempBox.getCorner000(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner001(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner010(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner011(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner100(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner101(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner110(tmpV)) != Plane.PlaneSide.Back) continue;
      if (frustum.planes[i].testPoint(tempBox.getCorner111(tmpV)) != Plane.PlaneSide.Back) continue;
      return false;
    }

    return true;
  }

  public Frustum getFrustum() {
    return frustum;
  }

  public void setFrustum(Frustum frustum) {
    this.frustum = frustum;
  }
}
