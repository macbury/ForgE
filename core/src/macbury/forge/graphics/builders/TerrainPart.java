package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
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
  private static final String TAG = "TerrainPart";
  public final Vector3i currentDirection  = new Vector3i();
  public final Vector3i voxelPosition     = new Vector3i();
  public final Vector3i voxelSize         = new Vector3i();
  public final Vector2  voxelSizeRect     = new Vector2();
  public Block block;
  public Voxel voxel;
  private final static Vector3i tempA = new Vector3i();
  private final static Vector3i tempB = new Vector3i();
  private final static Vector2 tempC  = new Vector2();
  public Block.Side face = Block.Side.all;

  @Override
  public void reset() {
    block = null;
    voxel = null;
    voxelSize.setZero();
    voxelPosition.setZero();
    currentDirection.setZero();
    voxelSizeRect.setZero();
  }

  private float distanceTo(TerrainPart otherPart) {
    tempA.set(this.voxelPosition).add(voxelSize);
    return otherPart.voxelPosition.dst(tempA);
  }

  private float distanceVeriticalTo(TerrainPart otherPart) {
    float dst = distanceTo(otherPart);
    if (dst != 1) {
      tempA.set(this.voxelPosition);
      return otherPart.voxelPosition.dst(tempA);
    } else {
      return dst;
    }
  }

  public boolean isHorizontalSimilar(TerrainPart otherPart) {
    getPartDirection(otherPart.voxelPosition, tempB);
    if (!voxelSize.isZero() && !currentDirection.equals(tempB)) {
      return false;
    }
    return canBeJoined(otherPart) && tempB.isOneDirection() && distanceVeriticalTo(otherPart) == 1;
  }

  public boolean isVeriticalSimilar(TerrainPart otherPart) {
    return canBeJoined(otherPart) && distanceVeriticalTo(otherPart) == 1;
  }

  private boolean canBeJoined(TerrainPart otherPart) {
    return block.blockShape.scalable && otherPart.block.id == this.block.id;
  }

  public void getPartDirection(Vector3i from, Vector3i out) {
    out.set(this.voxelPosition).sub(from).nor();
  }

  public void getUVScaling(Vector2 out) {
    voxelSizeRect.x = Math.max(voxelSizeRect.x, 1);
    voxelSizeRect.y = Math.max(voxelSizeRect.y, 1);
    out.set(voxelSizeRect);
    //Gdx.app.log("voxel size", out.toString());
  }

  public boolean joinVertical(TerrainPart otherPart) {
    if (canBeJoined(otherPart)) {
      getPartDirection(otherPart.voxelPosition, tempA);
      tempC.set(tempA.x, tempA.z).scl(otherPart.voxelSizeRect);

      //if (tempC.x == 0)
      voxelSize.add(tempC.x, 0, tempC.y);
      voxelSizeRect.add(tempC.x, tempC.y);

      return true;
    } else {
      return false;
    }
  }

  public void joinHorizontal(TerrainPart otherPart) {
    if (voxelSize.isZero()) {
      getPartDirection(otherPart.voxelPosition, tempA);
      currentDirection.set(tempA);
      //Gdx.app.log("Direction", currentDirection.toString());
    }

    voxelSize.add(currentDirection);

    voxelSizeRect.set(voxelSize.x, voxelSize.y);

    if (currentDirection.z == 1) {
      switch (face) {
        case right:
        case left:
          voxelSizeRect.set(voxelSize.z, voxelSize.y);
          break;
        case top:
          voxelSizeRect.set(voxelSize.x, voxelSize.z);
          break;
      }
    }

    voxelSizeRect.add(1, 1);
  }

  public int compareVertical(TerrainPart other) {
    if (other.voxelSizeRect.x > this.voxelSizeRect.x) {
      return -1;
    } else if (other.voxelSizeRect.x < this.voxelSizeRect.x) {
      return 1;
    } else {
      if (other.voxelSizeRect.y > this.voxelSizeRect.y) {
        return -1;
      } else if (other.voxelSizeRect.y < this.voxelSizeRect.y) {
        return 1;
      } else {
        if (other.voxelPosition.z > this.voxelPosition.z) {
          return -1;
        } else if (other.voxelPosition.z < this.voxelPosition.z) {
          return 1;
        } else {
          if (other.voxelPosition.x > this.voxelPosition.x) {
            return -1;
          } else if (other.voxelPosition.x < this.voxelPosition.x) {
            return 1;
          } else {
            if (other.voxelPosition.y > this.voxelPosition.y) {
              return -1;
            } else if (other.voxelPosition.y < this.voxelPosition.y) {
              return 1;
            } else {
              return 0;
            }
          }
        }
      }
    }
  }

  @Override
  public String toString() {
    return "<TerrainPart blockId="+this.block.id+" pos="+voxelPosition.toString()+" size="+voxelSizeRect+">";
  }
}
