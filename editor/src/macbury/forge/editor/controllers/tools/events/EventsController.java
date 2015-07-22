package macbury.forge.editor.controllers.tools.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.controllers.tools.inspector.properties.EditorScreenBeanInfo;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.EventSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.ChangeManagerListener;
import macbury.forge.editor.undo_redo.actions.PropertyChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.utils.Vector3i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by macbury on 25.03.15.
 */
public class EventsController implements SelectionInterface, ActionListener, OnMapChangeListener, MainToolbarController.EditorModeListener, ChangeManagerListener,  DefaultBeanBinder.PropertyChangeListener {

  private static final String TAG = "EventsController";
  private final JPopupMenu eventPopupMenu;
  private final JMenuItem mntmSetStartPosition;
  private final MainWindow mainWindow;
  private final EventSelection eventSelection;
  private final MapPropertySheet inspectorSheetPanel;
  private int levelId;
  private Vector3i selectedVoxelPosition;
  private SelectionSystem currentSelectionSystem;
  private LevelEditorScreen screen;
  private DefaultBeanBinder binder;
  private ChangeManager changeManager;

  public EventsController(MainWindow mainWindow) {
    this.mainWindow           = mainWindow;
    JPopupMenu.setDefaultLightWeightPopupEnabled(false); //Fix ugly bug with opengl canvas and popup
    this.eventPopupMenu       = new JPopupMenu();

    this.mntmSetStartPosition = new JMenuItem("Set start position");
    mntmSetStartPosition.addActionListener(this);
    eventPopupMenu.add(mntmSetStartPosition);
    this.eventSelection = new EventSelection();

    this.inspectorSheetPanel = new MapPropertySheet();
    mainWindow.objectInspectorContainerPanel.add(inspectorSheetPanel, BorderLayout.CENTER);
  }

  private void unbindInspector() {
    if (binder != null) {
      binder.unbind();
      stopListeningForPropertyChanges();
    }
    binder = null;
    inspectorSheetPanel.updateUI();
  }

  public void stopListeningForPropertyChanges() {
    this.binder.setListener(null);
  }

  public void startListeningForPropertyChanges() {
    this.binder.setListener(this);
  }

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
    if (changeManager != null)
      changeManager.removeListener(this);
    if (currentSelectionSystem != null)
      currentSelectionSystem.removeListener(this);
    currentSelectionSystem = null;
    levelId                = -1;
    changeManager          = null;
    screen                 = null;
    unbindInspector();
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    this.levelId           = screen.level.state.id;
    currentSelectionSystem = screen.selectionSystem;
    changeManager          = screen.changeManager;
    changeManager.addListener(this);
    this.screen            = screen;
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  @Override
  public void onEditorModeChange(MainToolbarController.EditorMode editorMode) {
    if (editorMode == MainToolbarController.EditorMode.Objects) {
      changeManager.removeListener(this);
      currentSelectionSystem.addListener(this);
      currentSelectionSystem.setSelection(eventSelection);
      binder = new DefaultBeanBinder(new EditorScreenBeanInfo.EditorScreenBean(screen), inspectorSheetPanel, new EditorScreenBeanInfo());

      if (binder != null) {
        inspectorSheetPanel.updateUI();
        startListeningForPropertyChanges();
      }
      changeManager.addListener(this);
    } else if (currentSelectionSystem != null){
      currentSelectionSystem.removeListener(this);
    }
  }

  @Override
  public void onPropertyChange(DefaultBeanBinder binder, PropertyChangeEvent event, Object object) {
    if (event.getNewValue() == null || event.getOldValue() == null) {
      return;
    }
    Gdx.app.log(TAG, "On property change event");
    stopListeningForPropertyChanges();
    PropertyChangeable propertyChangeable = new PropertyChangeable(object, event, this);
    changeManager.addChangeable(propertyChangeable).apply();
    startListeningForPropertyChanges();
  }

  @Override
  public void onChangeManagerChange(ChangeManager changeManager) {
    inspectorSheetPanel.updateUI();
  }
}
