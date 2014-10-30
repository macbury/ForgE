package macbury.forge.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import macbury.forge.graphics.VoxelMap;

/**
 * Created by macbury on 29.10.14.
 */
public class VoxelPicker {
  private static final String TAG = "VoxelPicker";
  private final VoxelMap map;
  private Vector3  rayEndPoint         = new Vector3();
  private Vector3i localVoxelPosition  = new Vector3i();
  private Vector3  worldVoxelPosition  = new Vector3();
  private Vector3  worldVoxelPositionWithSize  = new Vector3();
  private BoundingBox voxelBoundingBox = new BoundingBox();
  private Vector3  voxelBoxIntersection  = new Vector3();
  private Vector3 dir = new Vector3();
  private Vector3 sign = new Vector3();
  public VoxelPicker(VoxelMap map) {
    this.map = map;
  }

  public boolean getVoxelPositionForPickRay(Ray pickRay, float far, Vector3i outVoxelIntersectPoint) {
    for (float j = 0; j < far; j+=0.1f) {
      pickRay.getEndPoint(rayEndPoint, j);
      map.worldPositionToLocalVoxelPosition(rayEndPoint, localVoxelPosition);

      if (map.isSolid(localVoxelPosition)) {
        map.localVoxelPositionToWorldPosition(localVoxelPosition, worldVoxelPosition);
        worldVoxelPositionWithSize.set(worldVoxelPosition).add(map.tileSize);
        voxelBoundingBox.set(worldVoxelPosition, worldVoxelPositionWithSize);

        if (Intersector.intersectRayBounds(pickRay, voxelBoundingBox, voxelBoxIntersection)) {
          dir.set(voxelBoundingBox.getCenterX(), voxelBoundingBox.getCenterY(), voxelBoundingBox.getCenterZ()).sub(voxelBoxIntersection);

          sign.x = (dir.x > 0) ? 1 : (dir.x < 0 ? -1 : 0);
          sign.y = (dir.y > 0) ? 1 : (dir.y < 0 ? -1 : 0);
          sign.z = (dir.z > 0) ? 1 : (dir.z < 0 ? -1 : 0);

          dir.set(Math.round(Math.abs(dir.x)), Math.round(Math.abs(dir.y)), Math.round(Math.abs(dir.z))).scl(sign);
          worldVoxelPosition.sub(dir);

          outVoxelIntersectPoint.set(worldVoxelPosition);
        }

        return true;
      }
    }
    return false;
  }
}
