package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.level.Level;

/**
 * Created by macbury on 31.08.15.
 */
public class PostProcessingController implements OnMapChangeListener, DirectoryWatchJob.DirectoryWatchJobListener {
  private Level currentLevel;

  public PostProcessingController(DirectoryWatcher directoryWatcher) {
    directoryWatcher.addListener(PostProcessingManager.STORAGE_DIR, this);
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    currentLevel = null;
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    this.currentLevel = screen.level;
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    if (handle.extension().endsWith("glsl") || handle.extension().endsWith("json")) {
      Gdx.app.postRunnable(new Runnable() {
        @Override
        public void run() {
          reloadPostProcessing();
        }
      });
    }
  }

  private void reloadPostProcessing() {
    if (currentLevel != null) {
      try {
        currentLevel.postProcessing.reload();
        System.gc();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
