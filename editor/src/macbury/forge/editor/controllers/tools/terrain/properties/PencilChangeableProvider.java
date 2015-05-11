package macbury.forge.editor.controllers.tools.terrain.properties;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SingleBlockSelection;
import macbury.forge.editor.undo_redo.actions.ApplyBlock;
import macbury.forge.editor.undo_redo.actions.TerrainCursorChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 11.05.15.
 */
public class PencilChangeableProvider extends TerrainChangeableProvider<ApplyBlock> {
  public PencilChangeableProvider() {
    super(PencilChangeableProvider.class);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new SingleBlockSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {

  }

  @Override
  public ApplyBlock provide(ChunkMap map) {
    ApplyBlock applyBlock = new ApplyBlock(map);
    applyBlock.setSelection(getSelection());
    return applyBlock;
  }
}
