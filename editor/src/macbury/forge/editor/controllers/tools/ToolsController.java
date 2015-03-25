package macbury.forge.editor.controllers.tools;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.systems.SelectionSystem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;

/**
 * Created by macbury on 25.03.15.
 */
public class ToolsController implements ChangeListener, OnMapChangeListener {
  private final JTabbedPane toolsPane;
  private HashMap<Integer, ToolControllerListener> listeners;
  private SelectionSystem currentSelectionSystem;

  public ToolsController(JTabbedPane toolsPane) {
    listeners = new HashMap<Integer, ToolControllerListener>();
    this.toolsPane = toolsPane;
    toolsPane.addChangeListener(this);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    updateSelectedTabState();
  }

  private void updateSelectedTabState() {
    ToolControllerListener currentListener = listeners.get(toolsPane.getSelectedIndex());

    if (currentListener == null) {
      throw new GdxRuntimeException("Undefined tools controller for index: " + toolsPane.getSelectedIndex());
    }

    for (ToolControllerListener listener : listeners.values()) {
      if (listener == currentListener) {
        listener.onToolPaneSelected(currentListener, currentSelectionSystem);
      } else {
        listener.onToolPaneUnSelected(currentSelectionSystem);
      }
    }
  }

  public void register(ToolControllerListener toolController, Integer index) {
    listeners.put(index, toolController);
  }

  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {

  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    currentSelectionSystem = screen.selectionSystem;
    updateSelectedTabState();
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, EditorScreen editorScreen) {

  }

  public interface ToolControllerListener {
    public void onToolPaneUnSelected(SelectionSystem system);
    public void onToolPaneSelected(ToolControllerListener selectedToolController, SelectionSystem system);
  }
}
