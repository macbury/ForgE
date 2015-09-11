package macbury.forge.editor.reloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.editor.parell.JobManager;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

/**
 * Created by macbury on 29.10.14.
 */
public class AssetsChangeWatcher implements JNotifyListener, Disposable {
  private static final String TAG = "AssetsChangeWatcher";
  private final JobManager jobs;
  private int shaderWatchID;

  public AssetsChangeWatcher(JobManager jobs) {
    this.jobs = jobs;
    try {
      String path = Gdx.files.internal(".").path();
      ForgE.log(TAG, "Watching: " + path);
      this.shaderWatchID = JNotify.addWatch(path, JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED, true, this);
    } catch (JNotifyException e) {
      e.printStackTrace();
    }
  }

  public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
    print("renamed " + rootPath + " : " + oldName + " -> " + newName);
  }

  public void fileModified(int wd, String rootPath, String name) {
    print("modified " + rootPath + " : " + name);

    //reloadShaders();
  }

  private void reloadShaders() {

    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.shaders.reload();
      }
    });
  }

  public void fileDeleted(int wd, String rootPath, String name) {
    print("deleted " + rootPath + " : " + name);
  }

  public void fileCreated(int wd, String rootPath, String name) {
    print("created " + rootPath + " : " + name);
  }

  void print(String msg) {
    ForgE.log(TAG, msg);
  }

  @Override
  public void dispose() {
    try {
      JNotify.removeWatch(shaderWatchID);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}