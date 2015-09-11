package macbury.forge.level.loader;

import com.badlogic.gdx.Gdx;
import macbury.forge.ForgE;
import macbury.forge.level.LevelState;
import macbury.forge.promises.FutureTask;

/**
 * Created by macbury on 16.03.15.
 */
public class AsyncLevelLoader extends FutureTask<Integer, LevelState> {
  private static final String TAG = "AsyncLevelLoader";
  private int levelId;
  private Runnable runnable;

  @Override
  public void execute(Integer levelId) {
    this.levelId = levelId;
    ForgE.log(TAG, "Level id to load: " + levelId);
    this.runnable = new Runnable() {
      @Override
      public void run() {
        LevelState levelState = ForgE.levels.load(AsyncLevelLoader.this.levelId);
        done(levelState);
      }
    };
    Thread thread = new Thread(runnable);
    thread.run();
  }

}
