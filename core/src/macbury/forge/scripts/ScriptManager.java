package macbury.forge.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 12.05.15.
 */
public class ScriptManager implements Disposable, ScriptThread.Listener {
  public static final String SCRIPTS_DIR = "db/scripts/";
  public static final String FILE_EXT    = ".rb";
  private static final String TAG        = "ScriptManager";
  private  ScriptThread thread;

  public ScriptManager() {
    this.thread    = new ScriptThread(this);
  }

  public void loadAndRun() {
    thread.start("scripts:main.rb");
  }

  @Override
  public void dispose() {
    thread.dispose();
  }


  @Override
  public void onRubyError(Exception error) {
    error.printStackTrace();
    Gdx.app.exit();
  }
}
