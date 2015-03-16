package macbury.forge.editor.undo_redo.actions;

import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyRangeBlock extends TerrainCursorChangeable {
  private final Block block;
  private Voxel oldVoxels[][][];
  private Vector3i tempA = new Vector3i();

  public ApplyRangeBlock(AbstractSelection selection, VoxelMap map, Block currentBlock) {
    super(selection, map);
    this.block = currentBlock;
  }

  @Override
  public void revert() {
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          Voxel oldVoxel = oldVoxels[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z];
          map.setVoxelForPosition(oldVoxel, tempA.set(x,y,z));
        }
      }
    }

    oldVoxels = null;
  }

  @Override
  public void apply() {
    this.oldVoxels = new Voxel[(int)applyBox.getWidth()][(int)applyBox.getHeight()][(int)applyBox.getDepth()];
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          tempA.set(x,y,z);
          Voxel oldVoxel = map.getVoxelForPosition(tempA);
          if (oldVoxel != null) {
            oldVoxel = new Voxel(oldVoxel);
          }

          oldVoxels[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z] = oldVoxel;

          Voxel currentVoxel   = map.findOrInitializeVoxelForPosition(tempA);
          if (currentVoxel != null) {
            currentVoxel.blockId = block.id;
            currentVoxel.alginTo = alignToSide;
            map.setVoxelForPosition(currentVoxel, tempA);
          }
        }
      }
    }
  }
}
