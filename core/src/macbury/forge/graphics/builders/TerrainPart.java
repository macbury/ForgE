package macbury.forge.graphics.builders;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 30.03.15.
 */
public class TerrainPart implements Pool.Poolable {
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
  }

  public float distanceTo(TerrainPart otherPart) {
    tempA.set(this.voxelPosition).add(voxelSize);
    return otherPart.voxelPosition.dst(tempA);
  }

  public boolean similar(TerrainPart otherPart) {
    getPartDirection(otherPart.voxelPosition, tempB);
    return block.blockShape.scalable && otherPart.block.id == this.block.id && distanceTo(otherPart) == 1 && tempB.isOneDirection();
  }

  public void getPartDirection(Vector3i from, Vector3i out) {
    out.set(this.voxelPosition).add(voxelSize).sub(from).abs();
  }

  public void join(TerrainPart otherPart) {
    getPartDirection(otherPart.voxelPosition, tempA);
    voxelSize.add(tempA);

    //BoundingBox box = new BoundingBox();
    //box.ext();
  }
}
