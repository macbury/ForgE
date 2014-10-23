package macbury.forge.graphics.frustrum;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 05.03.14.
 */
public class DebugFrustrum extends Frustum {

  public DebugFrustrum(Frustum copy, Matrix4 invProjectionView) {
    super();
    for (int i = 0; i < copy.planes.length; i++) {
      planes[i] = new Plane(copy.planes[i].getNormal(), copy.planes[i].getD());
    }

    for (int i = 0; i < copy.planePoints.length; i++) {
      planePoints[i] = new Vector3(copy.planePoints[i].x, copy.planePoints[i].y, copy.planePoints[i].z);
    }

    update(invProjectionView);
  }
}
