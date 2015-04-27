package macbury.forge.terrain.greedy;

import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 26.04.15.
 */
public class GreedyMesh extends AbstractGreedyAlgorithm {
  private final ChunkMap map;
  private Vector3i nextTileToCheck = new Vector3i();

  public GreedyMesh(ChunkMap map) {
    super(ChunkMap.CHUNK_SIZE);
    this.map = map;
  }

  @Override
  public boolean isVoxelsTheSame(Voxel a, Voxel b) {
    return a != null && b != null && a.equals(b) && a.isScalable();
  }

  @Override
  protected Voxel maskFor(Block.Side face, int x, int y, int z) {
    if (map.isNotAir(x, y, z)) {
      nextTileToCheck.set(x, y, z);
      nextTileToCheck.add(face.direction);

      Voxel currentVoxel = map.getVoxelForPosition(x, y, z);
      Voxel nextVoxel    = map.getVoxelForPosition(nextTileToCheck);

      if (isVoxelTransparent(currentVoxel)) {
        if (!isVoxelTransparent(nextVoxel) && isVoxelBlockHaveOcculsion(nextVoxel)) {
          return null;
        } else if (nextVoxel == null || !isVoxelTransparent(nextVoxel) || nextVoxel.blockId != currentVoxel.blockId || !isVoxelBlockHaveOcculsion(currentVoxel)) {
          return currentVoxel;
        } else {
          return null;
        }
      } else if (isVoxelTransparent(nextVoxel)) {
        return currentVoxel;
      } else if (map.isEmptyNotOutOfBounds(nextTileToCheck) || doVoxelsDontHaveTheSameShape(currentVoxel, nextVoxel) || !isVoxelBlockHaveOcculsion(currentVoxel)) {
        return currentVoxel;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
