package macbury.forge.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 12.05.15.
 */
public class ScriptManager implements Disposable {
  public static final String SCRIPTS_DIR = "db/scripts/";
  public static final String FILE_EXT    = ".rb";
  private static final String TAG        = "ScriptManager";
  private  ScriptThread thread;

  public ScriptManager() {

  }

  public void loadAndRun(ScriptThread.Listener listener) {
    this.thread    = new ScriptThread(listener);
    thread.start("scripts:main.rb");
  }

  @Override
  public void dispose() {
    if (thread != null)
      thread.dispose();
  }

}
