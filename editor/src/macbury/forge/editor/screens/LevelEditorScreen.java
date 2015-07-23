package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
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
public class LevelEditorScreen extends AbstractScreen {
  private static final String TAG = "EditorScreen";
  private static final float LEVEL_EDITOR_FAR_CAMERA = 200;
  private final LevelState state;
  public Level level;
  private Stage stage;
  private RTSCameraController cameraController;
  private Overlay overlay;
  public SelectionSystem selectionSystem;
  public ChangeManager changeManager;
  private DecalBatch decalBatch;
  private Array<ForgeAfterRenderListener> afterRenderListenerArray = new Array<ForgeAfterRenderListener>();
  public LevelEditorScreen(LevelState state) {
    super();
    this.state = state;
  }

  @Override
  protected void initialize() {
    GLProfiler.enable();
    ForgE.config.buildColliders = false;
    this.overlay              = new Overlay();
    this.stage                = new Stage();
    this.changeManager        = new ChangeManager();
    this.level                = new Level(state);
    this.selectionSystem      = new SelectionSystem(level);
    this.cameraController     = new RTSCameraController();
    this.decalBatch           = new DecalBatch(new CameraGroupStrategy(level.camera));
    level.camera.far          = LEVEL_EDITOR_FAR_CAMERA;
    level.camera.near         = 0.01f;
    cameraController.setCenter(level.terrainMap.getWidth() / 2, level.terrainMap.getDepth() / 2);
    cameraController.setCamera(level.camera);
    cameraController.setOverlay(overlay);

    selectionSystem.setOverlay(overlay);
    level.entities.addSystem(selectionSystem);
    level.entities.psychics.disable();


    stage.addActor(overlay);


  }

  public void addAfterRenderListener(ForgeAfterRenderListener listener) {
    afterRenderListenerArray.add(listener);
  }

  public void removeAfterRenderListener(ForgeAfterRenderListener listener) {
    afterRenderListenerArray.removeValue(listener, true);
  }

  @Override
  public void render(float delta) {
    ForgE.assets.loadPendingInChunks();
    stage.act(delta);
    cameraController.update(delta);
    level.render(delta);
    stage.draw();

    for (int i = 0; i < afterRenderListenerArray.size; i++) {
      afterRenderListenerArray.get(i).forgeAfterRenderCallback(this);
    }

    GLProfiler.reset();
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
    GLProfiler.disable();
    level.dispose();
    changeManager.dispose();
    decalBatch.dispose();
  }

  public interface ForgeAfterRenderListener {
    public void forgeAfterRenderCallback(LevelEditorScreen screen);
  }
}
