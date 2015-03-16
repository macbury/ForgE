package macbury.forge.level.loader;

import macbury.forge.ForgE;
import macbury.forge.level.Level;
import macbury.forge.promises.GdxFutureTask;
import macbury.forge.promises.GdxPromiseFrameTicker;

/**
 * Created by macbury on 16.03.15.
 */
public class LevelAssetsLoaderTask extends GdxFutureTask<Level, Level> {
  private Level level;

  public LevelAssetsLoaderTask(GdxPromiseFrameTicker ticker) {
    super(ticker);
  }

  @Override
  public void execute(Level object) {
    super.execute(object);
    this.level = object;
  }

  @Override
  public void tick(float delta) {
    if (ForgE.assets.loadPendingInChunks()) {
      done(level);
    }
  }
}
