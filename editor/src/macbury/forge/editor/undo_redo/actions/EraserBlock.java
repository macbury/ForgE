package macbury.forge.editor.undo_redo.actions;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 06.11.14.
 */
public class EraserBlock extends TerrainCursorChangeable {
  private byte oldMaterials[][][];

  public EraserBlock(AbstractSelection selection, VoxelMap map) {
    super(selection, map);
  }

  @Override
  public void revert() {
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          byte blockId = oldMaterials[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z];
          map.setBlockIdForPosition(blockId, x,y,z);
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
          if (!map.isEmpty(x,y,z)) {
            oldMaterials[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z] = map.getBlockIdForPosition(x,y,z);
          }
          map.setEmptyForPosition(x,y,z);
        }
      }
    }
  }
}
