package macbury.forge.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import macbury.forge.level.Level;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 15.10.14.
 */
public class TestMeshScreen extends AbstractScreen {
  private CameraInputController cameraController;
  private Level level;

  @Override
  protected void initialize() {
    //this.level              = new Level();
    this.cameraController   = new CameraInputController(level.camera);

    Gdx.input.setInputProcessor(cameraController);
  }

  @Override
  public void render(float delta) {
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

  }

}
