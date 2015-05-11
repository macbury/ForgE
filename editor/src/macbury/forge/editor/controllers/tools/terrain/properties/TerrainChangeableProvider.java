package macbury.forge.editor.controllers.tools.terrain.properties;

import com.l2fprod.common.beans.BaseBeanInfo;
import macbury.forge.editor.controllers.tools.inspector.editors.AdvComboBoxPropertyEditor;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.controllers.tools.inspector.renderers.BrushImageIconRenderer;
import macbury.forge.editor.controllers.tools.terrain.BrushListRenderer;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.undo_redo.actions.TerrainCursorChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 11.05.15.
 */
public abstract class TerrainChangeableProvider<T extends TerrainCursorChangeable> extends BaseBeanInfo {
  private AbstractSelection selection;

  public TerrainChangeableProvider(Class type) {
    super(type);

  }

  protected abstract AbstractSelection buildSelection();

  public DefaultBeanBinder getPropertySheetBeanBinder(MapPropertySheet mapPropertySheet) {
    DefaultBeanBinder binder      = new DefaultBeanBinder(this, mapPropertySheet, this);
    mapPropertySheet.clearLosePropertyEditors();
    getPropertyEditorsAndRenderers(mapPropertySheet);

    return binder;
  }

  public abstract void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet);

  public AbstractSelection getSelection() {
    if (selection == null)
      selection = buildSelection();
    return selection;
  }

  public abstract T provide(ChunkMap map);
}
