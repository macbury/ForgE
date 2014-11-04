package macbury.forge.editor.undo_redo.actions;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.graphics.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyRangeBlock extends TerrainCursorChangeable {
  public ApplyRangeBlock(AbstractSelection selection, VoxelMap map) {
    super(selection, map);
  }

  @Override
  public void revert() {

  }

  @Override
  public void apply() {
    for (int x = (int)applyBox.min.x; x < applyBox.max.x; x++) {
      for (int y = (int)applyBox.min.y; y < applyBox.max.y; y++) {
        for (int z = (int)applyBox.min.z; z < applyBox.max.z; z++) {
          map.setMaterialForPosition(map.materials.get(4), x,y,z);
        }
      }
    }
  }
}
