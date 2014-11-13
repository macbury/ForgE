package macbury.forge.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 29.10.14.
 */
public class VoxelPicker {
  private static final String TAG = "VoxelPicker";
  private static final float CURSOR_TRESHOLD = 0.016f;
  private final VoxelMap map;
  private Vector3  rayEndPoint         = new Vector3();
  private Vector3i localVoxelPosition  = new Vector3i();
  private Vector3  worldVoxelPosition  = new Vector3();
  private Vector3  worldVoxelPositionWithSize  = new Vector3();
  private BoundingBox voxelBoundingBox = new BoundingBox();
  private Vector3  voxelBoxIntersection  = new Vector3();
  private Vector3 dir = new Vector3();
  private Vector3 finalDir = new Vector3();
  private Vector3 sign = new Vector3();
  private Vector3i originVoxelPosition = new Vector3i();

  public VoxelPicker(VoxelMap map) {
    this.map = map;
  }

  public boolean getVoxelPositionForPickRay(Ray pickRay, float far, VoxelCursor outVoxelIntersectPoint) {
    for (float j = 0; j < far; j+=0.1f) {
      pickRay.getEndPoint(rayEndPoint, j);
      map.worldPositionToLocalVoxelPosition(rayEndPoint, localVoxelPosition);

      if (map.isSolid(localVoxelPosition) || localVoxelPosition.y == 0) {
        map.localVoxelPositionToWorldPosition(localVoxelPosition, worldVoxelPosition);
        outVoxelIntersectPoint.replace.set(worldVoxelPosition);
        worldVoxelPositionWithSize.set(worldVoxelPosition).add(map.voxelSize);
        voxelBoundingBox.set(worldVoxelPosition, worldVoxelPositionWithSize);

        if (Intersector.intersectRayBounds(pickRay, voxelBoundingBox, voxelBoxIntersection)) {
          dir.set(voxelBoundingBox.getCenterX(), voxelBoundingBox.getCenterY(), voxelBoundingBox.getCenterZ()).sub(voxelBoxIntersection);

          sign.x = (dir.x > 0) ? 1 : (dir.x < 0 ? -1 : 0);
          sign.y = (dir.y > 0) ? 1 : (dir.y < 0 ? -1 : 0);
          sign.z = (dir.z > 0) ? 1 : (dir.z < 0 ? -1 : 0);

          dir.set(Math.abs(dir.x), Math.abs(dir.y), Math.abs(dir.z)).add(CURSOR_TRESHOLD, CURSOR_TRESHOLD, CURSOR_TRESHOLD);

          //Gdx.app.log(TAG, dir.toString());

          finalDir.set(Math.round(dir.x), Math.round(dir.y), Math.round(dir.z));
          worldVoxelPosition.sub(finalDir.scl(sign));

          outVoxelIntersectPoint.append.set(worldVoxelPosition);
        }

        return true;
      }
    }
    return false;
  }

  public boolean getVoxelPositionForPickRays(Ray pickRay, float far, VoxelCursor outVoxelIntersectPoint) {
    pickRay.getEndPoint(rayEndPoint, far);
    map.worldPositionToLocalVoxelPosition(rayEndPoint, localVoxelPosition);
    map.worldPositionToLocalVoxelPosition(pickRay.origin, originVoxelPosition);
    int dx = localVoxelPosition.x - originVoxelPosition.x;
    int dy = localVoxelPosition.y - originVoxelPosition.y;
    int dz = localVoxelPosition.z - originVoxelPosition.z;
    int ax = Math.abs(dx) << 1;
    int ay = Math.abs(dy) << 1;
    int az = Math.abs(dz) << 1;

    int signx = (int) Math.signum(dx);
    int signy = (int) Math.signum(dy);
    int signz = (int) Math.signum(dz);
    int x = originVoxelPosition.x;
    int y = originVoxelPosition.y;
    int z = originVoxelPosition.z;

    int deltax, deltay, deltaz;
    if (ax >= Math.max(ay, az)) /* x dominant */ {
      deltay = ay - (ax >> 1);
      deltaz = az - (ax >> 1);
      while (true) {
        if (isIntersectionPoint(pickRay, x, y, z, outVoxelIntersectPoint)) {
          return true;
        }
        if (x == localVoxelPosition.x) {
          return false;
        }
        if (deltay >= 0) {
          y += signy;
          deltay -= ax;
        }
        if (deltaz >= 0) {
          z += signz;
          deltaz -= ax;
        }
        x += signx;
        deltay += ay;
        deltaz += az;
      }
    } else if (ay >= Math.max(ax, az)) /* y dominant */ {
      deltax = ax - (ay >> 1);
      deltaz = az - (ay >> 1);
      while (true) {
        if (isIntersectionPoint(pickRay, x, y, z, outVoxelIntersectPoint)) {
          return true;
        }
        if (y == localVoxelPosition.y) {
          return false;
        }
        if (deltax >= 0) {
          x += signx;
          deltax -= ay;
        }
        if (deltaz >= 0) {
          z += signz;
          deltaz -= ay;
        }
        y += signy;
        deltax += ax;
        deltaz += az;
      }
    } else if (az >= Math.max(ax, ay)) /* z dominant */ {
      deltax = ax - (az >> 1);
      deltay = ay - (az >> 1);
      while (true) {
        if (isIntersectionPoint(pickRay, x, y, z, outVoxelIntersectPoint)) {
          return true;
        }
        if (z == localVoxelPosition.z) {
          return false;
        }
        if (deltax >= 0) {
          x += signx;
          deltax -= az;
        }
        if (deltay >= 0) {
          y += signy;
          deltay -= az;
        }
        z += signz;
        deltax += ax;
        deltay += ay;
      }
    }
    return false;
  }

  private boolean isIntersectionPoint(Ray pickRay, int x, int y, int z, VoxelCursor outVoxelIntersectPoint) {
    if (map.isSolid(x,y,z)) {
      localVoxelPosition.set(x,y,z);
      map.localVoxelPositionToWorldPosition(localVoxelPosition, worldVoxelPosition);
      outVoxelIntersectPoint.replace.set(worldVoxelPosition);
      worldVoxelPositionWithSize.set(worldVoxelPosition).add(map.voxelSize);
      voxelBoundingBox.set(worldVoxelPosition, worldVoxelPositionWithSize);

      if (Intersector.intersectRayBounds(pickRay, voxelBoundingBox, voxelBoxIntersection)) {
        dir.set(voxelBoundingBox.getCenterX(), voxelBoundingBox.getCenterY(), voxelBoundingBox.getCenterZ()).sub(voxelBoxIntersection);

        sign.x = (dir.x > 0) ? 1 : (dir.x < 0 ? -1 : 0);
        sign.y = (dir.y > 0) ? 1 : (dir.y < 0 ? -1 : 0);
        sign.z = (dir.z > 0) ? 1 : (dir.z < 0 ? -1 : 0);

        if (sign.isZero()) {
          sign.y = 1;
        }

        dir.set(Math.round(Math.abs(dir.x)), Math.round(Math.abs(dir.y)), Math.round(Math.abs(dir.z))).scl(sign);
        worldVoxelPosition.sub(dir);

        outVoxelIntersectPoint.append.set(worldVoxelPosition);
      }

      return true;
    }
    return false;
  }
}
