package macbury.forge.editor.undo_redo.actions;

import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SelectType;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyRangeBlock extends TerrainCursorChangeable {
  private final Block block;
  private byte oldMaterials[][][];

  public ApplyRangeBlock(AbstractSelection selection, VoxelMap map, Block currentBlock) {
    super(selection, map);
    this.block = currentBlock;
  }

  @Override
  public void revert() {
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          byte blockId = oldMaterials[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z];
          map.setBlockIdForPosition(blockId, x, y, z);

        }
      }
    }

    oldMaterials = null;
  }

  @Override
  public void apply() {
    this.oldMaterials = new byte[(int)applyBox.getWidth()][(int)applyBox.getHeight()][(int)applyBox.getDepth()];
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {

          if (selectType == SelectType.Append) {
            if (!map.isEmpty(x,y,z)) {
              oldMaterials[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z] = map.getBlockIdForPosition(x, y, z);
            }
            map.setBlockForPosition(block, x, y, z);
          } else if (!map.isEmpty(x,y,z)) {
            map.setBlockForPosition(block, x,y,z);
          }

        }
      }
    }
  }
}
