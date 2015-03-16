package macbury.forge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import macbury.forge.ForgE;
import macbury.forge.level.loader.AsyncLevelLoader;
import macbury.forge.level.Level;
import macbury.forge.level.loader.GeometryBuilderTask;
import macbury.forge.level.loader.LevelAssetsLoaderTask;
import macbury.forge.promises.FutureTask;
import macbury.forge.promises.GdxPromiseFrameTicker;
import macbury.forge.promises.Promise;

/**
 * Created by macbury on 16.03.15.
 */
public class LoadingScreen extends AbstractScreen implements Promise<Level> {
  private static final String TAG = "LoadingScreen";
  private final int levelId;
  private Level level;
  private GdxPromiseFrameTicker promiseTicker;
  private FPSLogger fpsLogger;

  public LoadingScreen(int levelId) {
    super();
    this.levelId = levelId;
  }

  @Override
  protected void initialize() {
    this.fpsLogger     = new FPSLogger();
    this.promiseTicker = new GdxPromiseFrameTicker();
    Gdx.app.log(TAG, "Preparing to load: " + levelId);
    AsyncLevelLoader loader                 = new AsyncLevelLoader();
    GeometryBuilderTask geometryBuilderTask = new GeometryBuilderTask(promiseTicker);
    loader.then(geometryBuilderTask);
    geometryBuilderTask.then(new LevelAssetsLoaderTask(promiseTicker)).then(this);
    loader.execute(levelId);
  }

  @Override
  public void render(float delta) {
    promiseTicker.update(delta);
    fpsLogger.log();
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    level = null;
  }

  @Override
  public void success(Level loadedLevel) {
    Gdx.app.log(TAG, "Done all promises!");
    GameplayScreen screen = new GameplayScreen(loadedLevel);
    ForgE.screens.set(screen);
  }

  @Override
  public void error(Exception reason) {

  }

}
