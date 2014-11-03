package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import macbury.forge.ForgE;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.systems.TerrainPainterSystem;
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
  public Level level;
  private Stage stage;
  private RTSCameraController cameraController;
  private Overlay overlay;
  public SelectionSystem selectionSystem;
  public TerrainPainterSystem terrainPainterSystem;
  public ChangeManager changeManager;

  @Override
  protected void initialize() {
    this.overlay              = new Overlay();
    this.stage                = new Stage();
    this.changeManager        = new ChangeManager();
    this.level                = new Level(LevelState.blank());
    this.selectionSystem      = new SelectionSystem(level);
    this.terrainPainterSystem = new TerrainPainterSystem(level, changeManager);
    this.cameraController     = new RTSCameraController();

    level.camera.far          = 200;

    cameraController.setCenter(level.terrainMap.getWidth() / 2, level.terrainMap.getDepth() / 2);
    cameraController.setCamera(level.camera);
    cameraController.setOverlay(overlay);

    selectionSystem.setOverlay(overlay);
    level.entities.addSystem(selectionSystem);
    level.entities.addSystem(terrainPainterSystem);
    stage.addActor(overlay);
    selectionSystem.addListener(terrainPainterSystem);
  }

  @Override
  public void render(float delta) {
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
    overlay.invalidateHierarchy();
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
