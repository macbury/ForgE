package macbury.forge.editor.controllers.tools.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.EventSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.utils.Vector3i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 25.03.15.
 */
public class EventsController implements SelectionInterface, ActionListener, OnMapChangeListener {

  private static final String TAG = "EventsController";
  private final JPopupMenu eventPopupMenu;
  private final JMenuItem mntmSetStartPosition;
  private final MainWindow mainWindow;
  private final EventSelection eventSelection;
  private int levelId;
  private Vector3i selectedVoxelPosition;

  public EventsController(MainWindow mainWindow) {
    this.mainWindow           = mainWindow;
    JPopupMenu.setDefaultLightWeightPopupEnabled(false); //Fix ugly bug with opengl canvas and popup
    this.eventPopupMenu       = new JPopupMenu();

    this.mntmSetStartPosition = new JMenuItem("Set start position");
    mntmSetStartPosition.addActionListener(this);
    eventPopupMenu.add(mntmSetStartPosition);
    this.eventSelection = new EventSelection();
  }
/*
  @Override
  public DefaultBeanBinder getBeanBinderForInspector(MapPropertySheet sheet) {
    return null;
  }

  @Override
  public void onToolPaneUnSelected(SelectionSystem system) {
    system.removeListener(this);
  }

  @Override
  public void onToolPaneSelected(ToolsController.ToolControllerListener selectedToolController, SelectionSystem system) {
    system.addListener(this);
    system.setSelection(eventSelection);
  }
*/
  @Override
  public void onSelectionStart(AbstractSelection selection) {

  }

  @Override
  public void onSelectionChange(AbstractSelection selection) {

  }

  @Override
  public void onSelectionEnd(AbstractSelection selection) {
    this.selectedVoxelPosition = selection.getEndPostion();
    if (selection.getSelectedMouseButton() == Input.Buttons.RIGHT) {
      Point point = mainWindow.getMousePosition();

      eventPopupMenu.show(mainWindow, (int)point.getX(), (int)point.getY());
    }

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == mntmSetStartPosition) {
      Gdx.app.log(TAG, "Selected position is: " + selectedVoxelPosition.toString());
      ForgE.db.setStartPosition(levelId, selectedVoxelPosition);
    }
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {

  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    this.levelId = screen.level.state.id;
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }
}
