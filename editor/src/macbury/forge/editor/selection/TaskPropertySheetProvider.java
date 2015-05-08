package macbury.forge.editor.selection;

import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.views.MapPropertySheet;

/**
 * Created by macbury on 08.05.15.
 */
public interface TaskPropertySheetProvider {
  public DefaultBeanBinder getPropertySheetBeanBinder(MapPropertySheet mapPropertySheet);
}
