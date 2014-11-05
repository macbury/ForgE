package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.voxel.VoxelMap;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 23.10.14.
 */
public class TerrainCursor {
  public final Vector3i cursor;
  public final Vector3i start;
  public final Vector3i end;
  public final Vector3 size;
  public final Vector3i sliceVec;
  public final Vector3i verticalVec;
  public final Vector3i horizontalVec;

  public TerrainCursor() {
    this.cursor    = new Vector3i();
    this.start     = new Vector3i();
    this.end       = new Vector3i();
    this.size      = new Vector3();
    sliceVec       = new Vector3i();
    verticalVec    = new Vector3i();
    horizontalVec  = new Vector3i();
  }

  public void set(Chunk chunk) {
    reset();
    setStartEnd(chunk.start, chunk.end);
  }

  public void set(VoxelMap map) {
    reset();
    setStartEnd(new Vector3i(), new Vector3i(map.getWidth(), map.getHeight(), map.getDepth()));
  }

  public void setStartEnd(Vector3i startVec, Vector3i endVec) {
    this.start.set(startVec);
    this.end.set(endVec);
  }

  public void setDirection(Vector3 sliceVec, Vector3 verticalVec, Vector3 horizontalVec) {
    this.sliceVec.set(sliceVec);
    this.verticalVec.set(verticalVec);
    this.verticalVec.set(horizontalVec);
  }

  public void reset() {
    this.cursor.setZero();
    this.start.setZero();
    this.end.setZero();
    size.setZero();
    sliceVec.setZero();
    verticalVec.setZero();
    horizontalVec.setZero();
  }
}
