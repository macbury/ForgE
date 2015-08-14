package macbury.forge.editor.parell.jobs;

import macbury.forge.ForgE;
import macbury.forge.editor.parell.Job;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;

/**
 * Created by macbury on 04.08.15.
 */
public class SaveLevelGeometryJob extends Job<Level> {
  private Level level;

  public SaveLevelGeometryJob(Level level) {
    super(Level.class);
    this.level = level;
  }

  @Override
  public boolean isBlockingUI() {
    return true;
  }

  @Override
  public boolean performCallbackOnOpenGlThread() {
    return false;
  }

  @Override
  public Level perform() {
    ForgE.levels.save(level.terrainGeometryProvider, level.state);
    this.level = null;
    return null;
  }

  @Override
  public void dispose() {
    level = null;
  }
}
