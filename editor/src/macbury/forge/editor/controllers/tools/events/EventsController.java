package macbury.forge.editor.controllers.tools.events;

import macbury.forge.editor.controllers.tools.ToolsController;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.systems.SelectionSystem;

/**
 * Created by macbury on 25.03.15.
 */
public class EventsController implements ToolsController.ToolControllerListener, SelectionInterface {
  @Override
  public void onToolPaneUnSelected(SelectionSystem system) {
    system.removeListener(this);
  }

  @Override
  public void onToolPaneSelected(ToolsController.ToolControllerListener selectedToolController, SelectionSystem system) {
    system.addListener(this);
    //system.setSelection(null);
  }

  @Override
  public void onSelectionStart(AbstractSelection selection) {

  }

  @Override
  public void onSelectionChange(AbstractSelection selection) {

  }

  @Override
  public void onSelectionEnd(AbstractSelection selection) {

  }
}
