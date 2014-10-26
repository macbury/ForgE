package macbury.forge.editor.controllers.listeners;

import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.screens.EditorScreen;

/**
 * Created by macbury on 25.10.14.
 */
public interface OnMapChangeListener {
  public void onNewMap(ProjectController controller, EditorScreen screen);
}
