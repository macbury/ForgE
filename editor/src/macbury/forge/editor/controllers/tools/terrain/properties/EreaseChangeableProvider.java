package macbury.forge.editor.controllers.tools.terrain.properties;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.EreaseSelection;
import macbury.forge.editor.undo_redo.actions.EraserBlock;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 11.05.15.
 */
public class EreaseChangeableProvider extends TerrainChangeableProvider<EraserBlock> {
  public EreaseChangeableProvider() {
    super(EreaseChangeableProvider.class);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new EreaseSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {

  }

  @Override
  public EraserBlock provide(ChunkMap map) {
    EraserBlock eraserBlock = new EraserBlock(map);
    eraserBlock.setSelection(getSelection());
    return eraserBlock;
  }
}
