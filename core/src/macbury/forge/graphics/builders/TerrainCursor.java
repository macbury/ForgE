package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.VoxelMap;
import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 23.10.14.
 */
public class TerrainCursor {
  public final Vector3Int cursor;
  public final Vector3Int start;
  public final Vector3Int end;
  public final Vector3 size;
  public final Vector3Int sliceVec;
  public final Vector3Int verticalVec;
  public final Vector3Int horizontalVec;

  public TerrainCursor() {
    this.cursor    = new Vector3Int();
    this.start     = new Vector3Int();
    this.end       = new Vector3Int();
    this.size      = new Vector3();
    sliceVec       = new Vector3Int();
    verticalVec    = new Vector3Int();
    horizontalVec  = new Vector3Int();
  }

  public void set(Chunk chunk) {
    reset();
    setStartEnd(chunk.start, chunk.end);
  }

  public void set(VoxelMap map) {
    reset();
    setStartEnd(new Vector3Int(), new Vector3Int(map.getWidth(), map.getHeight(), map.getDepth()));
  }

  public void setStartEnd(Vector3Int startVec, Vector3Int endVec) {
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
