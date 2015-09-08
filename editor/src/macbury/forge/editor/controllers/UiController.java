package macbury.forge.editor.controllers;

import com.badlogic.gdx.files.FileHandle;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.BuildUiJob;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.ui.UIManager;

/**
 * Created by macbury on 04.09.15.
 */
public class UiController implements DirectoryWatchJob.DirectoryWatchJobListener {

  private final JobManager jobs;

  public UiController(DirectoryWatcher directoryWatcher, JobManager jobs) {
    directoryWatcher.addListener(BuildUiJob.RAW_UI_IMAGES_PATH, this);
    this.jobs = jobs;
  }

  public void rebuild() {
    jobs.enqueue(new BuildUiJob());
  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    rebuild();
  }
}
