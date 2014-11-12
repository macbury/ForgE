package macbury.forge.editor.undo_redo.actions;

import macbury.forge.ForgE;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyBlock extends TerrainCursorChangeable {
  private byte oldBlockId;

  public ApplyBlock(AbstractSelection selection, VoxelMap map) {
    super(selection, map);
  }

  @Override
  public void revert() {
    map.setBlockIdForPosition(oldBlockId, from);
  }

  @Override
  public void apply() {
    oldBlockId = map.getBlockIdForPosition(from);
    //TODO: get values from picker!
    map.setBlockForPosition(ForgE.blocks.find(2), from);
  }
}
