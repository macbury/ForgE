package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 30.03.15.
 */
public class TerrainPart implements Pool.Poolable {
  private static final String TAG = "TerrainPart";
  public final Vector3i currentDirection  = new Vector3i();
  public final Vector3i voxelPosition     = new Vector3i();
  public final Vector3i voxelSize         = new Vector3i();
  public final Vector2 uvTiling          = new Vector2();
  public Block block;
  public Voxel voxel;

  public Block.Side face = Block.Side.all;

  @Override
  public void reset() {
    block = null;
    voxel = null;
    voxelSize.setZero();
    voxelPosition.setZero();
    currentDirection.setZero();
    uvTiling.setZero();
  }

  public void getUVScaling(Vector2 out) {
    uvTiling.x = Math.max(uvTiling.x, 1);
    uvTiling.y = Math.max(uvTiling.y, 1);
    out.set(uvTiling.x, uvTiling.y);
  }

  @Override
  public String toString() {
    return "<TerrainPart blockId="+this.block.id+" pos="+voxelPosition.toString()+" size="+voxelSize.toString()+">";
  }

}
