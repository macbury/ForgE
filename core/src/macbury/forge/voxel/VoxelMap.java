package macbury.forge.voxel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 17.10.14.
 */
public class VoxelMap implements Disposable {
  private final VoxelMaterial airMaterial;
  private final BoundingBox boundingBox;
  private final Vector3 temp = new Vector3();
  public final Vector3 voxelSize;
  public final Array<VoxelMaterial> materials;
  protected byte[][][] voxelMap;
  protected int width;
  protected int height;
  protected int depth;

  public VoxelMap(Vector3 voxelSize) {
    this.voxelSize = voxelSize;
    materials   = new Array<VoxelMaterial>();
    airMaterial = VoxelMaterial.air();
    boundingBox = new BoundingBox();
    materials.add(airMaterial);
  }

  public void initialize(int width, int height, int depth) {
    this.width  = width;
    this.height = height;
    this.depth  = depth;
    voxelMap    = new byte[width][height][depth];
  }

  public byte getColorIndexForPosition(int x, int y, int z) {
    if (isOutOfBounds(x,y,z)) {
      return 0;
    } else {
      return voxelMap[x][y][z];
    }
  }

  public void worldPositionToLocalVoxelPosition(Vector3 in, Vector3i out) {
    out.set(MathUtils.floor(in.x / voxelSize.x), MathUtils.floor(in.y / voxelSize.y), MathUtils.floor(in.z / voxelSize.z));
  }

  public void localVoxelPositionToWorldPosition(Vector3i in, Vector3 out) {
    out.set(in.x * voxelSize.x, in.y * voxelSize.y, in.z * voxelSize.z);
  }

  public VoxelMaterial getMaterialForPosition(int x, int y, int z) {
    int index = getColorIndexForPosition(x,y,z);
    return materials.get(index);
  }

  public void setMaterialForPosition(VoxelMaterial color, int x, int y, int z) {
    int index = materials.indexOf(color, true);
    if (index == -1) {
      materials.add(color);
      index = materials.size-1;
    }

    if (index > 255) {
      throw new GdxRuntimeException("Maximum materials for color pallete is finished!");
    }
    if (!isOutOfBounds(x,y,z)) {
      voxelMap[x][y][z] = (byte)index;
    }

  }

  public void setEmptyForPosition(int x, int y, int z) {
    setMaterialForPosition(airMaterial,x,y,z);
  }

  public boolean isEmpty(int x, int y, int z) {
    VoxelMaterial mat = getMaterialForPosition(x,y,z);
    return mat == null || mat.isAir();
  }

  public boolean isEmptyNotOutOfBounds(int x, int y, int z) {
    return !isOutOfBounds(x,y,z) && (isEmpty(x,y,z));
  }

  public boolean isOutOfBounds(int x, int y, int z) {
    if (x < 0 || y < 0 || z < 0) {
      return true;
    } else if (x >= width || y >= height || z >= depth) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void dispose() {

  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getDepth() {
    return depth;
  }

  public boolean isSolid(int x, int y, int z) {
    return !isEmpty(x,y,z);
  }

  public BoundingBox getBounds(Vector3 voxelSize) {
    temp.set(getWidth() * voxelSize.x, getHeight() * voxelSize.y, getDepth() * voxelSize.z);
    boundingBox.set(Vector3.Zero, temp);
    return boundingBox;
  }

  public boolean isSolid(Vector3i position) {
    return isSolid(position.x, position.y, position.z);
  }

  public boolean isSolid(Vector3 position) {
    return isSolid(Math.round(position.x), Math.round(position.y), Math.round(position.z));
  }

  public void setMaterialForPosition(VoxelMaterial voxelMaterial, Vector3i voxelPosition) {
    setMaterialForPosition(voxelMaterial, voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public VoxelMaterial getMaterialForPosition(Vector3i voxelPosition) {
    return getMaterialForPosition(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public boolean isEmpty(Vector3i voxelPosition) {
    return isEmpty(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public void setEmptyForPosition(Vector3i voxelPosition) {
    setEmptyForPosition(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public boolean isEmptyNotOutOfBounds(Vector3i voxelPosition) {
    return isEmptyNotOutOfBounds(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public boolean isOutOfBounds(Vector3i voxelPosition) {
    return isOutOfBounds(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }
}
