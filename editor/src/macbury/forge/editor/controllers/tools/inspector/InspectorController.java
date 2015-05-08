package macbury.forge.editor.controllers.tools.inspector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.controllers.tools.inspector.properties.EditorScreenBeanInfo;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.ChangeManagerListener;
import macbury.forge.editor.undo_redo.actions.PropertyChangeable;
import macbury.forge.editor.views.MapPropertySheet;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

/**
 * Created by macbury on 15.03.15.
 */
public class InspectorController implements OnMapChangeListener, DefaultBeanBinder.PropertyChangeListener, ChangeManagerListener, MainToolbarController.EditorModeListener {
  private static final String TAG = "InspectorController";
  private final MapPropertySheet inspectorSheetPanel;
  private LevelEditorScreen screen;
  private DefaultBeanBinder binder;
  private ChangeManager changeManager;
  private boolean pause;

  public InspectorController(JPanel mapSettingsPanel) {
    this.inspectorSheetPanel = new MapPropertySheet();
    mapSettingsPanel.add(inspectorSheetPanel);
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    screen.changeManager.removeListener(this);
    this.screen = null;
    unbind();
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    this.screen   = screen;
    changeManager = screen.changeManager;

    changeManager.addListener(this);
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  @Override
  public void onChangeManagerChange(ChangeManager changeManager) {
    inspectorSheetPanel.updateUI();
  }


  private void unbind() {
    if (binder != null) {
      binder.unbind();
      stopListeningForPropertyChanges();
    }
    binder = null;
    inspectorSheetPanel.updateUI();
  }

  @Override
  public void onPropertyChange(DefaultBeanBinder binder, PropertyChangeEvent event, Object object) {
    Gdx.app.log(TAG, "On property change event");
    stopListeningForPropertyChanges();
    //PropertyChangeable propertyChangeable = new PropertyChangeable(object, event, this);
   // changeManager.addChangeable(propertyChangeable).apply();
    startListeningForPropertyChanges();
  }

  public void stopListeningForPropertyChanges() {
    this.binder.setListener(null);
  }

  public void startListeningForPropertyChanges() {
    this.binder.setListener(this);
  }

  @Override
  public void onEditorModeChange(MainToolbarController.EditorMode editorMode) {
    /*unbind();
    if (editorMode == MainToolbarController.EditorMode.Objects) {
      if (screen != null) {
        binder = selectedToolController.getBeanBinderForInspector(inspectorSheetPanel);
        //this.binder   = new DefaultBeanBinder(new EditorScreenBeanInfo.EditorScreenBean(screen), inspectorSheetPanel, new EditorScreenBeanInfo());
        if (binder != null) {
          inspectorSheetPanel.updateUI();
          startListeningForPropertyChanges();
        }
      }
    }*/
  }
}
