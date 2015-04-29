package macbury.forge.terrain.greedy;

import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 27.04.15.
 */
public class GreedyCollider extends AbstractGreedyAlgorithm {
  private final ChunkMap map;
  private Vector3i nextTileToCheck = new Vector3i();

  public GreedyCollider(ChunkMap map) {
    super(ChunkMap.CHUNK_SIZE);
    this.map = map;
  }

  @Override
  public boolean isVoxelsTheSame(Voxel a, Voxel b) {
    return a != null && b != null && b.getBlock().isSolid() && a.getBlock().isSolid() && b.getBlock().blockShape == b.getBlock().blockShape && a.isScalable() && b.isScalable();
  }

  @Override
  protected Voxel maskFor(Block.Side face, int x, int y, int z) {
    Voxel currentVoxel = map.getVoxelForPosition(x, y, z);

    if (currentVoxel != null && currentVoxel.getBlock().isSolid()) {
      nextTileToCheck.set(x, y, z);
      nextTileToCheck.add(face.direction);
      Voxel nextVoxel    = map.getVoxelForPosition(nextTileToCheck);
      if (map.isEmptyNotOutOfBounds(nextTileToCheck) || nextVoxel != null && nextVoxel.getBlock().isNotSolid()) {
        return currentVoxel;
      } else {
        return null;
      }
    } else {
      return null;
    }

  }
}
