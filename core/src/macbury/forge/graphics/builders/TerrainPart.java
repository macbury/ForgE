package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 30.03.15.
 */
public class TerrainPart implements Pool.Poolable {
  public final Vector3i currentDirection = new Vector3i();
  public final Vector3i voxelPosition = new Vector3i();
  public final Vector3i voxelSize      = new Vector3i();
  public Block block;
  public Voxel voxel;
  private final static Vector3i tempA = new Vector3i();
  private final static Vector3i tempB = new Vector3i();

  @Override
  public void reset() {
    block = null;
    voxel = null;
    voxelSize.setZero();
    voxelPosition.setZero();
    currentDirection.setZero();
  }

  public float distanceTo(TerrainPart otherPart) {
    tempA.set(this.voxelPosition).add(voxelSize);
    return otherPart.voxelPosition.dst(tempA);
  }

  public boolean isHorizontalSimilar(TerrainPart otherPart) {
    getPartDirection(otherPart.voxelPosition, tempB);
    if (!voxelSize.isZero() && !currentDirection.equals(tempB)) {
      return false;
    }
    return canBeJoined(otherPart) && tempB.isOneDirection() && distanceTo(otherPart) == 1;
  }

  private boolean canBeJoined(TerrainPart otherPart) {
    return (block.blockShape.scalable) && otherPart.block.id == this.block.id;
  }

  public boolean isVeriticalSimilar(TerrainPart otherPart) {
    return false;//canBeJoined(otherPart) && otherPart.voxelPosition.dst(this.voxelPosition) == 1;
  }

  public boolean comparedTo(TerrainPart other) {
    if (this.voxelPosition.y != other.voxelPosition.y)
      return this.voxelPosition.y < other.voxelPosition.y;
    if (this.voxelPosition.x != other.voxelPosition.x)
      return this.voxelPosition.x < other.voxelPosition.x;
    if (this.voxelPosition.z != other.voxelPosition.z)
      return this.voxelPosition.z < other.voxelPosition.z;
    if (this.voxelSize.x != other.voxelSize.x)
      return this.voxelSize.x < other.voxelSize.x;
    if (this.voxelSize.y != other.voxelSize.y)
      return this.voxelSize.y < other.voxelSize.y;
    return this.voxelSize.z < other.voxelSize.z;
  }

  public void getPartDirection(Vector3i from, Vector3i out) {
    out.set(this.voxelPosition).add(voxelSize).sub(from).abs();
  }

  public void getUVScaling(Vector2 out) {
    if (currentDirection.equals(Vector3i.FRONT)) {
      out.set(voxelSize.z, voxelSize.y);
    } else {
      out.set(voxelSize.x, voxelSize.z);
    }

  }

  public void join(TerrainPart otherPart) {
    getPartDirection(otherPart.voxelPosition, tempA);
    if (voxelSize.isZero()) {
      currentDirection.set(tempA);
    }

    voxelSize.add(currentDirection);

    //BoundingBox box = new BoundingBox();
    //box.ext();
  }



}
