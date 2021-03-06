package macbury.forge.editor.undo_redo.actions;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public abstract class TerrainCursorChangeable extends CursorChangeable {
  protected VoxelMap map;

  public TerrainCursorChangeable(VoxelMap map) {
    this.map = map;
  }

  @Override
  public void dispose() {
    map = null;
  }
}
