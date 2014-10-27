package macbury.forge.ray;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 26.10.14.
 */
public class VoxelIntersector {
  private final Vector3 size = new Vector3();
  private final Vector3 off = new Vector3();
  private final Vector3 pos = new Vector3();
  private final Vector3 dir = new Vector3();

  private final Vector3Int index = new Vector3Int();

  private final Vector3 delta = new Vector3();
  private final Vector3Int sign = new Vector3Int();
  private final Vector3 max = new Vector3();

  private int limit;
  private int plotted;

  public VoxelIntersector(Vector3 voxelSize) {
    size.set(voxelSize);
  }

  public void begin(Vector3Int offset, Ray ray, int limit) {
    off.set( offset.x, offset.y, offset.z );
    this.limit = limit;

    pos.set( ray.origin );
    dir.set( ray.direction ).nor();

    delta.set(size.x / dir.x, size.y / dir.y, size.z / dir.z);

    sign.x = (dir.x > 0) ? 1 : (dir.x < 0 ? -1 : 0);
    sign.y = (dir.y > 0) ? 1 : (dir.y < 0 ? -1 : 0);
    sign.z = (dir.z > 0) ? 1 : (dir.z < 0 ? -1 : 0);

    reset();
  }

  public boolean next() {
    if (plotted++ > 0) {
      float mx = sign.x * max.x;
      float my = sign.y * max.y;
      float mz = sign.z * max.z;

      if (mx < my && mx < mz) {
        max.x += delta.x;
        index.x += sign.x;
      } else if (mz < my && mz < mx) {
        max.z += delta.z;
        index.z += sign.z;
      } else {
        max.y += delta.y;
        index.y += sign.y;
      }
    }
    return (plotted <= limit);
  }

  public Vector3Int get(){
    return index;
  }

  private void reset() {
    plotted = 0;

    index.x = (int)Math.floor((pos.x - off.x) / size.x);
    index.y = (int)Math.floor((pos.y - off.y) / size.y);
    index.z = (int)Math.floor((pos.z - off.z) / size.z);

    float ax = index.x * size.x + off.x;
    float ay = index.y * size.y + off.y;
    float az = index.z * size.z + off.z;

    max.x = (sign.x > 0) ? ax + size.x - pos.x : pos.x - ax;
    max.y = (sign.y > 0) ? ay + size.y - pos.y : pos.y - ay;
    max.z = (sign.z > 0) ? az + size.z - pos.z : pos.z - az;
    max.set( max.x / dir.x, max.y / dir.y, max.z / dir.z );
  }

  public void end() {
    plotted = limit + 1;
  }

}
