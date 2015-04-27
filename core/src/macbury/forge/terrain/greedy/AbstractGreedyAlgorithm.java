package macbury.forge.terrain.greedy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 26.04.15.
 */
public abstract class AbstractGreedyAlgorithm implements Disposable {
  private final Voxel mask[];
  private final int size;
  private final Array<GreedyQuad> result = new Array<GreedyQuad>();
  private static Pool<GreedyQuad> greedyQuadPool = new Pool<GreedyQuad>() {
    @Override
    protected GreedyQuad newObject() {
      GreedyQuad part = new GreedyQuad();
      part.reset();
      return part;
    }
  };

  public AbstractGreedyAlgorithm(int size) {
    this.size  = size;
    mask = new Voxel[size * size];
  }

  private void resetMask() {
    for (int i = 0; i < mask.length; i++) {
      mask[i] = null;
    }
  }

  protected boolean isVoxelTransparent(Voxel voxel) {
    return voxel != null && voxel.getBlock().transparent;
  }

  protected boolean isVoxelBlockHaveOcculsion(Voxel voxel) {
    return voxel != null && voxel.getBlock().blockShape.occulsion;
  }

  protected boolean doVoxelsDontHaveTheSameShape(Voxel voxelA, Voxel voxelB) {
    return voxelB != null && (voxelB.getBlock().blockShape != voxelA.getBlock().blockShape);
  }

  public void greedy(Block.Side face, Vector3i origin) {
    greedyQuadPool.freeAll(result);
    result.clear();

    for (int a = 0; a < size; a++) {
      int n = 0;
      for (int b = 0; b < size; b++) {
        for (int c = 0; c < size; c++) {
          switch (face) {
            case top:
            case bottom:
              n = createMask(face, n, c + origin.x, a + origin.y,b + origin.z);
              break;
            case left:
            case right:
              n = createMask(face, n, a + origin.x, c + origin.y, b + origin.z);
              break;
            default:
              n = createMask(face, n, b + origin.x, c + origin.y, a + origin.z);
              break;
          }
        }
      }

      createQuads(face, a, origin);
    }
  }

  public void getResults(Array<GreedyQuad> output) {
    output.clear();
    output.addAll(result);
  }

  private boolean isAir(Voxel voxel) {
    return voxel == null || voxel.isAir();
  }

  public abstract boolean isVoxelsTheSame(Voxel a, Voxel b);

  private void createQuads(Block.Side face, int a, Vector3i origin) {
    int n = 0;

    for(int j = 0; j < ChunkMap.CHUNK_SIZE; j++) {
      for (int i = 0; i < ChunkMap.CHUNK_SIZE; ) {
        if (mask[n] == null || mask[n].isAir()) {
          i++;
          n++;
        } else {
          int w, h = 0;
          for(w = 1; i + w < ChunkMap.CHUNK_SIZE && !isAir(mask[n + w]) && isVoxelsTheSame(mask[n + w], mask[n]); w++) {}

          boolean done = false;

          for(h = 1; j + h < ChunkMap.CHUNK_SIZE; h++) {

            for(int k = 0; k < w; k++) {
              if(isAir(mask[n + k + h * ChunkMap.CHUNK_SIZE]) || !isVoxelsTheSame(mask[n + k + h * ChunkMap.CHUNK_SIZE], mask[n])) { done = true; break; }
            }

            if(done) { break; }
          }

          //Gdx.app.log(TAG, "New quad: " + mask[n].blockId + " size=" + w+"x"+h + " at " + "X: " + i + " Y: " + j);

          GreedyQuad currentPart      = greedyQuadPool.obtain();
          currentPart.face            = face;
          currentPart.block           = mask[n].getBlock();
          currentPart.voxel           = mask[n];

          if (face == Block.Side.front || face == Block.Side.back) {
            currentPart.uvTiling.set(h,w);
            currentPart.voxelPosition.set(j, i, a);
            currentPart.voxelSize.set(h, w, 1);

          } else if (face == Block.Side.left || face == Block.Side.right) {
            currentPart.uvTiling.set(h,w);
            currentPart.voxelPosition.set(a, i, j);
            currentPart.voxelSize.set(1, w, h);
          } else {
            currentPart.uvTiling.set(w,h);
            currentPart.voxelPosition.set(i, a, j);
            currentPart.voxelSize.set(w, 1, h);
          }

          currentPart.voxelPosition.add(origin);

          result.add(currentPart);

          //Gdx.app.log(TAG, "Quad: " + currentPart.toString() + " with origin " + origin.toString());

          for(int l = 0; l < h; ++l) {
            for(int k = 0; k < w; ++k) { mask[n + k + l * ChunkMap.CHUNK_SIZE] = null; }
          }

          i += w;
          n += w;
        }
      }
    }
  }

  private int createMask(Block.Side face, int n, int x, int y, int z) {
    mask[n++] = maskFor(face, x,y,z);
    return n;
  }

  protected abstract Voxel maskFor(Block.Side face, int x, int y, int z);

  @Override
  public void dispose() {
    greedyQuadPool.freeAll(result);
    resetMask();
    result.clear();
  }

  public boolean haveResults() {
    return result.size > 0;
  }

  public static class GreedyQuad implements Pool.Poolable {
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
      return "<GreedyQuad blockId="+this.block.id+" pos="+voxelPosition.toString()+" size="+voxelSize.toString()+">";
    }
  }
}
