package macbury.forge.editor.parell.jobs;

import macbury.forge.ForgE;
import macbury.forge.editor.parell.Job;
import macbury.forge.level.LevelState;

/**
 * Created by macbury on 06.11.14.
 */
public class NewLevelJob extends Job<LevelState> {

  private final LevelState state;
  private final String storeDir;

  public NewLevelJob(LevelState state, String storeDir) {
    super(LevelState.class);
    this.state = state;
    this.storeDir = storeDir;
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
    state.bootstrap();
    ForgE.levels.save(state, storeDir);
    return state;
  }
}
