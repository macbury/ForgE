package macbury.forge.graphics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by macbury on 17.10.14.
 */
public class VoxelMap implements Disposable {
  private final ColorMaterial airMaterial;
  private final BoundingBox boundingBox;
  private final Vector3 temp = new Vector3();
  protected Array<ColorMaterial> materials;
  protected byte[][][] voxelMap;
  protected int width;
  protected int height;
  protected int depth;

  public VoxelMap() {
    materials   = new Array<ColorMaterial>();
    airMaterial = ColorMaterial.air();
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
    if (x < 0 || y < 0 || z < 0) {
      return 0;
    } else if (x >= width || y >= height || z >= depth) {
      return 0;
    } else {
      return voxelMap[x][y][z];
    }
  }

  public ColorMaterial getMaterialForPosition(int x, int y, int z) {
    int index = getColorIndexForPosition(x,y,z);
    return materials.get(index);
  }

  public void setMaterialForPosition(ColorMaterial color, int x, int y, int z) {
    int index = materials.indexOf(color, true);
    if (index == -1) {
      materials.add(color);
      index = materials.size-1;
    }

    if (index > 255) {
      throw new GdxRuntimeException("Maximum materials for color pallete is finished!");
    }

    voxelMap[x][y][z] = (byte)index;
  }

  public void setEmptyForPosition(int x, int y, int z) {
    setMaterialForPosition(airMaterial,x,y,z);
  }

  public boolean isEmpty(int x, int y, int z) {
    ColorMaterial mat = getMaterialForPosition(x,y,z);
    return mat == null || mat.isAir();
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
}
