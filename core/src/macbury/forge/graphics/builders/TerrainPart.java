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
public class TerrainPart implements Pool.Poolable, Comparable<TerrainPart> {
  private static final String TAG = "TerrainPart";
  public final Vector3i currentDirection  = new Vector3i();
  public final Vector3i voxelPosition     = new Vector3i();
  public final Vector3i voxelSize         = new Vector3i();
  public final Vector2  voxelSizeRect     = new Vector2();
  public Block block;
  public Voxel voxel;
  private final static Vector3i tempA = new Vector3i();
  private final static Vector3i tempB = new Vector3i();
  private final static Vector3i tempE = new Vector3i();
  private final static BoundingBox tempBoxA = new BoundingBox();
  private final static BoundingBox tempBoxB = new BoundingBox();
  private final static Vector3  tempC = new Vector3();
  private final static Vector3  tempD = new Vector3();
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
      getBoundingBox(tempBoxA);
      otherPart.getBoundingBox(tempBoxB);
      tempB.set(voxelSize).scl(tempA);
      tempE.set(otherPart.voxelSize).scl(tempA);
      if ( tempB.equals(tempE) && tempBoxA.contains(tempBoxB)) {
        tempBoxA.ext(tempBoxB);
        tempBoxA.getMin(tempD);
        voxelPosition.set(tempD);
        voxelSize.set((int)tempBoxA.getWidth(), (int)tempBoxA.getHeight(), (int)tempBoxA.getDepth());
        //voxelSizeRect.add(tempC.x, tempC.y);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private void getBoundingBox(BoundingBox out) {
    this.voxelPosition.cpyTo(tempC);
    this.voxelPosition.cpyTo(tempD).add(this.voxelSize.x, this.voxelSize.y, this.voxelSize.z);
    out.set(tempC, tempD);
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
    return (int)other.voxelSizeRect.x - (int)this.voxelSizeRect.x;
  }

  @Override
  public String toString() {
    return "<TerrainPart blockId="+this.block.id+" pos="+voxelPosition.toString()+" size="+voxelSize.toString()+">";
  }


  @Override
  public int compareTo(TerrainPart o) {
    float dst = tempA.set(voxelPosition).dst2(o.voxelPosition);
    return dst < 0 ? -1 : (dst > 0 ? 1 : 0);
  }
}
