package macbury.forge.editor.controllers.listeners;

import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.screens.LevelEditorScreen;

/**
 * Created by macbury on 25.10.14.
 */
public interface OnMapChangeListener {
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen);
  public void onNewMap(ProjectController controller, LevelEditorScreen screen);
  public void onProjectStructureChange(ProjectController controller);
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen);
}
