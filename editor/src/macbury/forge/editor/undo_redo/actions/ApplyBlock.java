package macbury.forge.editor.undo_redo.actions;

import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyBlock extends TerrainCursorChangeable {
  private final Block block;
  private byte oldBlockId;

  public ApplyBlock(AbstractSelection selection, VoxelMap map, Block currentBlock) {
    super(selection, map);
    this.block = currentBlock;
  }

  @Override
  public void revert() {
    map.setBlockIdForPosition(oldBlockId, from);
  }

  @Override
  public void apply() {
    oldBlockId = map.getBlockIdForPosition(from);
    map.setBlockForPosition(block, from);
  }
}
