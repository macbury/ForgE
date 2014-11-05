package macbury.forge.editor.undo_redo.actions;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.VoxelMap;
import macbury.forge.voxel.VoxelMaterial;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyRangeBlock extends TerrainCursorChangeable {
  private VoxelMaterial oldMaterials[][][];

  public ApplyRangeBlock(AbstractSelection selection, VoxelMap map) {
    super(selection, map);
  }

  @Override
  public void revert() {
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          VoxelMaterial mat = oldMaterials[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z];
          if (mat == null) {
            map.setEmptyForPosition(x,y,z);
          } else {
            map.setMaterialForPosition(mat, x,y,z);
          }

        }
      }
    }

    oldMaterials = null;
  }

  @Override
  public void apply() {
    this.oldMaterials = new VoxelMaterial[(int)applyBox.getWidth()][(int)applyBox.getHeight()][(int)applyBox.getDepth()];
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          if (!map.isEmpty(x,y,z)) {
            oldMaterials[x - (int)applyBox.min.x][y - (int)applyBox.min.y][z - (int)applyBox.min.z] = map.getMaterialForPosition(x,y,z);
          }
          map.setMaterialForPosition(map.materials.get(4), x,y,z);
        }
      }
    }
  }
}
