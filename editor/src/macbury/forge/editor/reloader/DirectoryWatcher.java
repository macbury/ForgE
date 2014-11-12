package macbury.forge.editor.reloader;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 12.11.14.
 */
public class DirectoryWatcher implements Disposable {
  private final Array<DirectoryWatchJob> directories;

  public DirectoryWatcher() {
    this.directories = new Array<DirectoryWatchJob>();
  }

  public void start() {
    for (DirectoryWatchJob job : directories) {
      job.start();
    }
  }

  private DirectoryWatchJob find(String path) {
    for (DirectoryWatchJob job : directories) {
      if (job.path == path) {
        return job;
      }
    }

    return null;
  }

  public void addListener(String path, DirectoryWatchJob.DirectoryWatchJobListener listener) {
    DirectoryWatchJob job = find(path);
    if (job == null) {
      job = new DirectoryWatchJob(path);
      directories.add(job);
    }
    job.addListener(listener);

  }

  public void removeListener(String path, DirectoryWatchJob.DirectoryWatchJobListener listener) {
    DirectoryWatchJob job = find(path);
    if (job != null) {
      job.removeListener(listener);

    }
  }

  @Override
  public void dispose() {
    for (DirectoryWatchJob job : directories) {
      job.dispose();
    }

    directories.clear();
  }

}
