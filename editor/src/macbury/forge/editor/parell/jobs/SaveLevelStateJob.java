package macbury.forge.editor.parell.jobs;

import macbury.forge.ForgE;
import macbury.forge.editor.parell.Job;
import macbury.forge.level.LevelState;

/**
 * Created by macbury on 10.03.15.
 */
public class SaveLevelStateJob extends Job<LevelState> {

  private LevelState state;

  public SaveLevelStateJob(LevelState state) {
    super(LevelState.class);
    this.state = state;
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
    ForgE.levels.save(state);
    return state;
  }

  @Override
  public void dispose() {
    state = null;
  }
}
