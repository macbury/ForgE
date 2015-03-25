package macbury.forge.editor.controllers.tools.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.db.models.PlayerStartPosition;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.controllers.tools.ToolsController;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.EventSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 25.03.15.
 */
public class EventsController implements ToolsController.ToolControllerListener, SelectionInterface, ActionListener, OnMapChangeListener {

  private final JPopupMenu eventPopupMenu;
  private final JMenuItem mntmSetStartPosition;
  private final MainWindow mainWindow;
  private final EventSelection eventSelection;
  private int levelId;

  public EventsController(MainWindow mainWindow) {
    this.mainWindow           = mainWindow;
    JPopupMenu.setDefaultLightWeightPopupEnabled(false); //Fix ugly bug with opengl canvas and popup
    this.eventPopupMenu       = new JPopupMenu();

    this.mntmSetStartPosition = new JMenuItem("Set start position");
    mntmSetStartPosition.addActionListener(this);
    eventPopupMenu.add(mntmSetStartPosition);
    this.eventSelection = new EventSelection();
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

  @Override
  public void onSelectionStart(AbstractSelection selection) {

  }

  @Override
  public void onSelectionChange(AbstractSelection selection) {

  }

  @Override
  public void onSelectionEnd(AbstractSelection selection) {
    if (selection.getSelectedMouseButton() == Input.Buttons.RIGHT) {
      Point point = mainWindow.getMousePosition();

      eventPopupMenu.show(mainWindow, (int)point.getX(), (int)point.getY());
    }

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == mntmSetStartPosition) {
      ForgE.db.setStartPosition(levelId, eventSelection.getStartPosition());
    }
  }

  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {

  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    this.levelId = screen.level.state.id;
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, EditorScreen editorScreen) {

  }
}
