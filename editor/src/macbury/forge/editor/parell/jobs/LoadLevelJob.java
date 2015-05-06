package macbury.forge.editor.parell.jobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.editor.parell.Job;
import macbury.forge.level.LevelState;

/**
 * Created by macbury on 10.03.15.
 */
public class LoadLevelJob extends Job<LevelState> {
  private final FileHandle levelToLoad;

  public LoadLevelJob(FileHandle levelToLoad) {
    super(LevelState.class);
    this.levelToLoad = levelToLoad;
  }

  @Override
  public boolean isBlockingUI() {
    return true;
  }

  @Override
  public boolean performCallbackOnOpenGlThread() {
    return true;
  }

  @Override
  public LevelState perform() {
    LevelState state = ForgE.levels.load(levelToLoad);
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.assets.unloadUnusedAssets();
      }
    });
    return state;
  }
}
