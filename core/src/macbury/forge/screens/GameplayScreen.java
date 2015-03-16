package macbury.forge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.level.Level;

/**
 * Created by macbury on 16.03.15.
 */
public class GameplayScreen extends AbstractScreen {
  private static final float FAR_CAMERA = 45;
  private static final float NEAR_CAMERA = 0.01f;
  private final Level level;
  private FirstPersonCameraController cameraController;

  public GameplayScreen(Level level) {
    this.level = level;
  }

  @Override
  protected void initialize() {
    this.cameraController = new FirstPersonCameraController(level.camera);
    level.camera.position.set(50, 3, 50);
    level.camera.far  = FAR_CAMERA;
    level.camera.near = NEAR_CAMERA;
    //level.camera.lookAt(22,0,22);
    Gdx.input.setInputProcessor(cameraController);
  }

  @Override
  public void render(float delta) {
    cameraController.update(delta);
    level.render(delta);
  }

  @Override
  public void resize(int width, int height) {
    level.resize(width, height);
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
    level.dispose();
  }
}
