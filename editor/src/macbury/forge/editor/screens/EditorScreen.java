package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.level.Level;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 18.10.14.
 */
public class EditorScreen extends AbstractScreen {
  private CameraInputController cameraController;
  private Level level;

  @Override
  protected void initialize() {
    this.level              = new Level();
    level.camera.position.set(0,15,15);
    level.camera.lookAt(Vector3.Zero);
    level.camera.update(true);
    this.cameraController   = new CameraInputController(level.camera);

    Gdx.input.setInputProcessor(cameraController);
  }

  @Override
  public void render(float delta) {
    ForgE.graphics.clearAll(Color.BLACK);
    cameraController.update();
    level.update(delta);
    level.render();
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

  public Level getLevel() {
    return level;
  }
}
