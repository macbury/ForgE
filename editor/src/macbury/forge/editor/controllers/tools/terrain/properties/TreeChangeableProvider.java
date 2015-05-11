package macbury.forge.editor.controllers.tools.terrain.properties;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SingleBlockSelection;
import macbury.forge.editor.undo_redo.actions.TerrainCursorChangeable;
import macbury.forge.editor.undo_redo.actions.TreeBuilderChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 11.05.15.
 */
public class TreeChangeableProvider extends TerrainChangeableProvider<TreeBuilderChangeable> {
  public TreeChangeableProvider() {
    super(TreeChangeableProvider.class);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new SingleBlockSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {

  }

  @Override
  public TreeBuilderChangeable provide(ChunkMap map) {
    TreeBuilderChangeable changeable = new TreeBuilderChangeable(map);
    changeable.setSelection(getSelection());
    return changeable;
  }
}
