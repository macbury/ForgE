package macbury.forge.editor.controllers.inspector;

import com.badlogic.gdx.Gdx;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.controllers.inspector.properties.EditorScreenBeanInfo;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.ChangeManagerListener;
import macbury.forge.editor.undo_redo.actions.PropertyChangeable;
import macbury.forge.editor.views.MapPropertySheet;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by macbury on 15.03.15.
 */
public class InspectorController implements OnMapChangeListener, DefaultBeanBinder.PropertyChangeListener, ChangeManagerListener {
  private static final String TAG = "InspectorController";
  private final MapPropertySheet inspectorSheetPanel;
  private EditorScreen screen;
  private DefaultBeanBinder binder;
  private ChangeManager changeManager;
  private boolean pause;

  public InspectorController(JPanel mapSettingsPanel) {
    this.inspectorSheetPanel = new MapPropertySheet();
    mapSettingsPanel.add(inspectorSheetPanel);
  }

  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {
    screen.changeManager.removeListener(this);
    this.screen = null;
    if (binder != null){
      binder.unbind();
    }
  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    this.screen   = screen;
    changeManager = screen.changeManager;

    changeManager.addListener(this);

    this.binder   = new DefaultBeanBinder(new EditorScreenBeanInfo.EditorScreenBean(screen), inspectorSheetPanel, new EditorScreenBeanInfo());
    this.binder.setListener(this);
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, EditorScreen editorScreen) {

  }

  @Override
  public void onPropertyChange(DefaultBeanBinder binder, PropertyChangeEvent event, Object object) {
    this.pause = PropertyChangeable.class.isInstance(changeManager.getCurrent());
    if (!pause) {
      PropertyChangeable propertyChangeable = new PropertyChangeable(object, event);
      changeManager.addChangeable(propertyChangeable).apply();
    }
    pause = false;
    Gdx.app.log(TAG, "Property change: "+ event.getNewValue() + " from " + event.getOldValue() + " for " + object.toString());
  }

  @Override
  public void onChangeManagerChange(ChangeManager changeManager) {
    inspectorSheetPanel.updateUI();
  }
}
