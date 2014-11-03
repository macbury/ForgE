package macbury.forge.editor.undo_redo.actions;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.graphics.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyBlock extends TerrainCursorChangeable {
  public ApplyBlock(AbstractSelection selection, VoxelMap map) {
    super(selection, map);
  }

  @Override
  public void revert() {

  }

  @Override
  public void apply() {
    map.setMaterialForPosition(map.materials.get(3), from.append);
  }
}
