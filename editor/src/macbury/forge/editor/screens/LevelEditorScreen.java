package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.graphics.camera.RTSCameraController;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;
import macbury.forge.screens.AbstractScreen;
import macbury.forge.terrain.geometry.DynamicGeometryProvider;
import macbury.forge.ui.DebugFrameBufferResult;
import macbury.forge.ui.FullScreenFrameBufferResult;
import macbury.forge.ui.Overlay;

/**
 * Created by macbury on 18.10.14.
 */
public class LevelEditorScreen extends AbstractScreen {
  private static final String TAG = "EditorScreen";
  private static final float LEVEL_EDITOR_FAR_CAMERA = 120;
  private final LevelState state;
  public Level level;
  private RTSCameraController cameraController;
  private Overlay overlay;
  public SelectionSystem selectionSystem;
  public ChangeManager changeManager;
  private DecalBatch decalBatch;
  private Array<ForgeAfterRenderListener> afterRenderListenerArray = new Array<ForgeAfterRenderListener>();
  private boolean pause = false;
  private FullScreenFrameBufferResult fullScreenBuffer;

  public LevelEditorScreen(LevelState state, JobManager jobs) {
    super();
    this.state = state;
    this.changeManager        = new ChangeManager(jobs);
  }

  @Override
  protected void onInitialize() {
    GLProfiler.enable();
    this.overlay              = new Overlay();

    this.level                = new Level(state, new DynamicGeometryProvider(state.terrainMap));
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

    this.fullScreenBuffer = new FullScreenFrameBufferResult(Fbo.FRAMEBUFFER_FINAL);
    //previewFbos();
  }

  public void previewFbos() {
    Array<String> fbos = ForgE.fb.all().keys().toArray();
    int size = Gdx.graphics.getWidth() / (fbos.size + 1);

    for (int i = 0; i < fbos.size; i++) {
      ForgE.ui.addActor(DebugFrameBufferResult.build(fbos.get(i), size, i * size, 0));
    }
  }

  public void addAfterRenderListener(ForgeAfterRenderListener listener) {
    afterRenderListenerArray.add(listener);
  }

  public void removeAfterRenderListener(ForgeAfterRenderListener listener) {
    afterRenderListenerArray.removeValue(listener, true);
  }

  @Override
  public void render(float delta) {
    if (!pause) {
      ForgE.assets.loadPendingInChunks();
      cameraController.update(delta);
      level.render(delta);

      for (int i = 0; i < afterRenderListenerArray.size; i++) {
        afterRenderListenerArray.get(i).forgeAfterRenderCallback(this);
      }
    }
    GLProfiler.reset();
  }

  @Override
  public void resize(int width, int height) {
    Gdx.app.log(TAG, "Resize: "+ width +"x"+height);

    level.resize(width, height);
    overlay.invalidate();
  }

  @Override
  public void show() {
    ForgE.ui.addActor(fullScreenBuffer);
    ForgE.ui.addActor(overlay);
  }

  @Override
  public void hide() {
    overlay.remove();
    fullScreenBuffer.remove();
  }

  @Override
  public void pause() {
    this.pause = true;
  }

  @Override
  public void resume() {
    this.pause = false;
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
