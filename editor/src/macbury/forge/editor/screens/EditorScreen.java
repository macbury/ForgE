package macbury.forge.editor.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import macbury.forge.ForgE;
import macbury.forge.editor.systems.EditorSystem;
import macbury.forge.graphics.camera.RTSCameraController;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;
import macbury.forge.screens.AbstractScreen;
import macbury.forge.ui.Overlay;

/**
 * Created by macbury on 18.10.14.
 */
public class EditorScreen extends AbstractScreen {
  public Level level;
  private Stage stage;
  private RTSCameraController cameraController;
  private Overlay overlay;
  public EditorSystem editorSystem;

  @Override
  protected void initialize() {
    this.overlay            = new Overlay();
    this.stage              = new Stage();
    this.level              = new Level(LevelState.blank());
    this.editorSystem       = new EditorSystem(level);
    level.camera.far        = 512;
    this.cameraController   = new RTSCameraController();
    cameraController.setCenter(level.terrainMap.getWidth() / 2, level.terrainMap.getDepth() / 2);
    cameraController.setCamera(level.camera);
    cameraController.setOverlay(overlay);

    editorSystem.setOverlay(overlay);
    level.entities.addSystem(editorSystem);
    stage.addActor(overlay);
  }

  @Override
  public void render(float delta) {
    stage.act(delta);
    cameraController.update(delta);
    ForgE.graphics.clearAll(Color.BLACK);
    level.render(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    level.resize(width, height);
  }

  @Override
  public void show() {
    ForgE.input.addProcessor(stage);
  }

  @Override
  public void hide() {
    ForgE.input.removeProcessor(stage);
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
