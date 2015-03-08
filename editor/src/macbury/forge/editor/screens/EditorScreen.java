package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import macbury.forge.ForgE;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.graphics.camera.RTSCameraController;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;
import macbury.forge.screens.AbstractScreen;
import macbury.forge.ui.Overlay;

/**
 * Created by macbury on 18.10.14.
 */
public class EditorScreen extends AbstractScreen {
  private static final String TAG = "EditorScreen";
  private static final float LEVEL_EDITOR_FAR_CAMERA = 200;
  private final LevelState state;
  public Level level;
  private Stage stage;
  private RTSCameraController cameraController;
  private Overlay overlay;
  public SelectionSystem selectionSystem;
  public ChangeManager changeManager;

  public EditorScreen(LevelState state) {
    super();
    this.state = state;
  }

  @Override
  protected void initialize() {
    this.overlay              = new Overlay();
    this.stage                = new Stage();
    this.changeManager        = new ChangeManager();
    this.level                = new Level(state);
    this.selectionSystem      = new SelectionSystem(level);
    this.cameraController     = new RTSCameraController();
    level.camera.far          = LEVEL_EDITOR_FAR_CAMERA;

    cameraController.setCenter(level.terrainMap.getWidth() / 2, level.terrainMap.getDepth() / 2);
    cameraController.setCamera(level.camera);
    cameraController.setOverlay(overlay);

    selectionSystem.setOverlay(overlay);
    level.entities.addSystem(selectionSystem);
    stage.addActor(overlay);
  }

  @Override
  public void render(float delta) {
    ForgE.assets.loadPending();
    stage.act(delta);
    cameraController.update(delta);
    level.render(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    Gdx.app.log(TAG, "Resize: "+ width +"x"+height);

    level.resize(width, height);
    stage.getViewport().update(width,height);
    overlay.invalidate();
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
    changeManager.dispose();
  }

}
