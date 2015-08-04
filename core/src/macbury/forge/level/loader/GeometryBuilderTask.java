package macbury.forge.level.loader;

import com.badlogic.gdx.Gdx;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;
import macbury.forge.promises.FutureTask;
import macbury.forge.promises.GdxFutureTask;
import macbury.forge.promises.GdxPromiseFrameTicker;
import macbury.forge.promises.Promise;

/**
 * Created by macbury on 16.03.15.
 */
public class GeometryBuilderTask extends GdxFutureTask<LevelState, Level> {
  private static final String TAG = "GeometryBuilderTask";
  private static final int CHUNK_TO_REBUILD_PER_TICK = 20;
  private LevelState levelState;
  private Level level;

  public GeometryBuilderTask(GdxPromiseFrameTicker ticker) {
    super(ticker);
  }

  @Override
  public void execute(LevelState object) {
    super.execute(object);
    this.levelState = object;
    //this.level      = new Level(levelState);
    Gdx.app.log(TAG, "Initializing level!");
  }

  @Override
  public void tick(float delta) {
    if (level.terrainEngine.rebuild(CHUNK_TO_REBUILD_PER_TICK, false)) {
      done(level);
    }
  }
}
