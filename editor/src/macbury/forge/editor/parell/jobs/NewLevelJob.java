package macbury.forge.editor.parell.jobs;

import macbury.forge.editor.parell.Job;
import macbury.forge.level.LevelState;

/**
 * Created by macbury on 06.11.14.
 */
public class NewLevelJob extends Job<LevelState> {

  public NewLevelJob() {
    super(LevelState.class);
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
    LevelState newLevelState = LevelState.blank(500, 100, 500);
    return newLevelState;
  }
}
