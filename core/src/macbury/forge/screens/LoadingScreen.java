package macbury.forge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.PerformanceCounter;
import macbury.forge.ForgE;
import macbury.forge.db.models.Teleport;
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
  private static final float ROTATION_SPEED = 80f;
  private static final float STANDARD_CUBE_ROTATION = 60F;
  private Teleport teleport;
  private PerspectiveCamera camera;
  private Level level;
  private GdxPromiseFrameTicker promiseTicker;
  private FPSLogger fpsLogger;
  private ShapeRenderer shapeRenderer;
  private Matrix4 boxTransMat;
  private float indicatorRotation;

  public LoadingScreen(Teleport teleport) {
    super();
    this.teleport = teleport;
  }

  @Override
  protected void onInitialize() {
    this.shapeRenderer = new ShapeRenderer();
    this.camera        = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.fpsLogger     = new FPSLogger();
    this.promiseTicker = new GdxPromiseFrameTicker();
    this.boxTransMat   = new Matrix4();

    camera.position.set(0, 8, 0);
    camera.lookAt(Vector3.Zero);
    Gdx.app.log(TAG, "Preparing to load: " + teleport.mapId);
    AsyncLevelLoader loader                 = new AsyncLevelLoader();
    GeometryBuilderTask geometryBuilderTask = new GeometryBuilderTask(promiseTicker);
    loader.then(geometryBuilderTask);
    geometryBuilderTask.then(new LevelAssetsLoaderTask(promiseTicker)).then(this);
    loader.execute(teleport.mapId);

    this.indicatorRotation = 0f;
  }

  @Override
  public void render(float delta) {
    ForgE.graphics.clearAll(Color.BLACK);
    promiseTicker.update(delta);
    fpsLogger.log();

    indicatorRotation += ROTATION_SPEED * delta;
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
      shapeRenderer.setProjectionMatrix(camera.combined);
      shapeRenderer.identity();
      shapeRenderer.translate(6, 0, 4);
      shapeRenderer.rotate(0,0,1, STANDARD_CUBE_ROTATION);
      shapeRenderer.rotate(0,0,1, indicatorRotation);
      shapeRenderer.box(-0.5f,-0.5f,-0.5f, 1, 1, 1);
    }shapeRenderer.end();
  }

  @Override
  public void resize(int width, int height) {
    camera.update();
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
    level    = null;
    teleport = null;
  }

  @Override
  public void success(Level loadedLevel) {
    Gdx.app.log(TAG, "Done all promises!");
    GameplayScreen screen = new GameplayScreen(teleport, loadedLevel);
    ForgE.screens.set(screen);
  }

  @Override
  public void error(Exception reason) {

  }

}
