package macbury.forge.editor.controllers.tools.terrain.properties;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SingleBlockSelection;
import macbury.forge.editor.undo_redo.actions.FloodFillChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 05.08.15.
 */
public class LevelFloodBrushChangeableProvider extends TerrainChangeableProvider<FloodFillChangeable> {
  public LevelFloodBrushChangeableProvider() {
    super(LevelFloodBrushChangeableProvider.class);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new SingleBlockSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {

  }

  @Override
  public FloodFillChangeable provide(ChunkMap map) {
    FloodFillChangeable changeable = new FloodFillChangeable(map, true);
    changeable.setSelection(getSelection());
    return changeable;
  }
}
