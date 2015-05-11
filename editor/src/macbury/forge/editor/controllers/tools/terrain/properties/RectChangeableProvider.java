package macbury.forge.editor.controllers.tools.terrain.properties;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.BoxSelection;
import macbury.forge.editor.undo_redo.actions.ApplyRangeBlock;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 11.05.15.
 */
public class RectChangeableProvider extends TerrainChangeableProvider<ApplyRangeBlock> {
  public RectChangeableProvider() {
    super(RectChangeableProvider.class);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new BoxSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {

  }

  @Override
  public ApplyRangeBlock provide(ChunkMap map) {
    ApplyRangeBlock rangeBlock = new ApplyRangeBlock(map);
    rangeBlock.setSelection(getSelection());
    return rangeBlock;
  }
}
