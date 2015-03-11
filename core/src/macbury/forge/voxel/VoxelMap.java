package macbury.forge.voxel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.procedular.PerlinNoise;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 17.10.14.
 */
public class VoxelMap implements Disposable {
  private static final byte AIR_BLOCK_INDEX = 0;
  private final BoundingBox boundingBox;
  private final Vector3 temp = new Vector3();
  public final Vector3 voxelSize;
  protected BlocksProvider blocks;
  protected Voxel[][][] voxelMap;
  protected int width;
  protected int height;
  protected int depth;

  public VoxelMap(Vector3 voxelSize, BlocksProvider blocksProvider) {
    this.voxelSize  = voxelSize;
    boundingBox     = new BoundingBox();
    this.blocks     = blocksProvider;
  }


  public void initialize(int width, int height, int depth) {
    this.width  = width;
    this.height = height;
    this.depth  = depth;
    voxelMap    = new Voxel[width][height][depth];
  }

  public byte getBlockIdForPosition(int x, int y, int z) {
    if (isOutOfBounds(x,y,z) || voxelMap[x][y][z] == null) {
      return AIR_BLOCK_INDEX;
    } else {
      return voxelMap[x][y][z].blockId;
    }
  }

  public Block getBlockForPosition(int x, int y, int z) {
    return blocks.find(getBlockIdForPosition(x,y,z));
  }

  public void worldPositionToLocalVoxelPosition(Vector3 in, Vector3i out) {
    out.set(MathUtils.floor(in.x / voxelSize.x), MathUtils.floor(in.y / voxelSize.y), MathUtils.floor(in.z / voxelSize.z));
  }

  public void localVoxelPositionToWorldPosition(Vector3i in, Vector3 out) {
    out.set(in.x * voxelSize.x, in.y * voxelSize.y, in.z * voxelSize.z);
  }

  public Voxel setBlockIdForPosition(byte blockId, int x, int y, int z) {
    if (!isOutOfBounds(x,y,z)) {
      if (blockId == AIR_BLOCK_INDEX) {
        voxelMap[x][y][z] = null;
        return null;
      } else {
        if (voxelMap[x][y][z] == null) {
          voxelMap[x][y][z]     = new Voxel();
        }

        voxelMap[x][y][z].blockId = blockId;
        return voxelMap[x][y][z];
      }
    }
    return null;
  }

  public void setBlockForPosition(Block block, int x, int y, int z) {
    if (block == null) {
      setBlockIdForPosition(AIR_BLOCK_INDEX, x, y, z);
    } else {
      setBlockIdForPosition(block.id, x,y,z);
    }

  }

  public void setEmptyForPosition(int x, int y, int z) {
    setBlockForPosition(null, x, y, z);
  }

  public boolean isEmpty(int x, int y, int z) {
    Block block = getBlockForPosition(x,y,z);
    return block == null || block.isAir();
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
    return getBlockForPosition(x,y,z).solid;
  }

  public boolean isTransparent(int x, int y, int z) {
    return getBlockForPosition(x,y,z).transparent;
  }

  public BoundingBox getBounds(Vector3 voxelSize) {
    temp.set(getWidth() * voxelSize.x, getHeight() * voxelSize.y, getDepth() * voxelSize.z);
    boundingBox.set(Vector3.Zero, temp);
    return boundingBox;
  }


  public void setBlockForPosition(Block block, Vector3i voxelPosition) {
    setBlockForPosition(block, voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public Block getBlockForPosition(Vector3i voxelPosition) {
    return getBlockForPosition(voxelPosition.x, voxelPosition.y, voxelPosition.z);
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

  public void setBlockIdForPosition(byte blockId, Vector3i voxelPosition) {
    setBlockIdForPosition(blockId, voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public byte getBlockIdForPosition(Vector3i voxelPosition) {
    return getBlockIdForPosition(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public boolean isTransparent(Vector3i voxelPosition) {
    return isTransparent(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public boolean isNotAir(int x, int y, int z) {
    return !isEmpty(x,y,z);
  }

  public boolean isNotAir(Vector3i position) {
    return isNotAir(position.x, position.y, position.z);
  }

  public boolean isNotAir(Vector3 position) {
    return isNotAir(Math.round(position.x), Math.round(position.y), Math.round(position.z));
  }

  public Voxel getVoxelForPosition(int x, int y, int z) {
    if (isOutOfBounds(x,y,z)) {
      return null;
    } else {
      return voxelMap[x][y][z];
    }
  }

  public Voxel getVoxelForPosition(Vector3i voxelPosition) {
    return getVoxelForPosition(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  public void setAlignmentForPosition(Block.Side alignToSide, Vector3i voxelPosition) {
    Voxel voxel = getVoxelForPosition(voxelPosition);
    if (voxel != null) {
      voxel.alginTo = alignToSide;
    }
  }

  public void setVoxelForPosition(Voxel voxel, Vector3i voxelPosition) {
    if (!isOutOfBounds(voxelPosition)) {
      voxelMap[voxelPosition.x][voxelPosition.y][voxelPosition.z] = voxel;
    }
  }

  public Voxel findOrInitializeVoxelForPosition(Vector3i voxelPosition) {
    if (!isOutOfBounds(voxelPosition)) {
      if (voxelMap[voxelPosition.x][voxelPosition.y][voxelPosition.z] == null) {
        voxelMap[voxelPosition.x][voxelPosition.y][voxelPosition.z] = new Voxel();
      }

      return voxelMap[voxelPosition.x][voxelPosition.y][voxelPosition.z];
    } else {
      return null;
    }
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

}
