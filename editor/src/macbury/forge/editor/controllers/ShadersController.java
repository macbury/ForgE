package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.shaders.utils.ShadersManager;

/**
 * Created by macbury on 12.11.14.
 */
public class ShadersController implements DirectoryWatchJob.DirectoryWatchJobListener {
  public ShadersController(DirectoryWatcher directoryWatcher) {
    directoryWatcher.addListener(ShadersManager.SHADERS_PATH, this);
  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.shaders.reload();
      }
    });
  }
}
