package macbury.forge.editor.reloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import javax.swing.*;

/**
 * Created by macbury on 12.11.14.
 */
public class DirectoryWatchJob implements Disposable, JNotifyListener {
  private static final String TAG = "DirectoryWatchJob";
  public final String path;
  private final Array<DirectoryWatchJobListener> listeners;
  private int jnotifyWatchId;
  private FileHandle currentHandle;

  public DirectoryWatchJob(String path) {
    this.path      = path;
    this.listeners = new Array<DirectoryWatchJobListener>();
  }

  @Override
  public void dispose() {
    listeners.clear();
    try {
      JNotify.removeWatch(jnotifyWatchId);
    } catch (Exception e) {
      //e.printStackTrace();
    }
  }

  public void addListener(DirectoryWatchJobListener listener) {
    listeners.add(listener);
  }

  public void removeListener(DirectoryWatchJobListener listener) {
    listeners.removeValue(listener, true);
  }

  private void triggerListener(String path, String file) {
    FileHandle handle = Gdx.files.internal(path + file);

    if (handle.extension().endsWith("___jb_bak___") || handle.extension().endsWith("___jb_old___") || handle.extension().endsWith("~")) {
      Gdx.app.log(TAG, "Skipping: " + handle.name());
    } else {
      currentHandle = handle;
      Gdx.app.log(TAG, "Change for: " + currentHandle.name());
    }


  }

  public void process() {
    if (currentHandle != null) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          executeTrigger();
        }
      });
    }
  }

  private void executeTrigger() {
    Gdx.app.log(TAG, "Execute trigger: " + currentHandle.name() + " for " + listeners.size);
    for (DirectoryWatchJobListener listener : listeners) {
      listener.onFileInDirectoryChange(currentHandle);
    }

    currentHandle = null;
  }

  public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
    triggerListener(rootPath, newName);
  }

  public void fileModified(int wd, String rootPath, String name) {
    triggerListener(rootPath, name);
  }

  public void fileDeleted(int wd, String rootPath, String name) {
    triggerListener(rootPath, name);
  }

  public void fileCreated(int wd, String rootPath, String name) {
    triggerListener(rootPath, name);
  }

  public void start() {
    try {
      Gdx.app.log(TAG, "Watching: " + path);
      this.jnotifyWatchId = JNotify.addWatch(path, JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED, true, this);
    } catch (JNotifyException e) {
      e.printStackTrace();
    }
  }

  public interface DirectoryWatchJobListener {
    public void onFileInDirectoryChange(FileHandle handle);
  }
}
