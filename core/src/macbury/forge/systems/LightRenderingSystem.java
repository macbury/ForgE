package macbury.forge.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RenderableComponent;
import macbury.forge.components.ShadowComponent;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.FrameBufferManager;
import macbury.forge.level.Level;
import macbury.forge.level.LevelEnv;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.utils.ActionTimer;

/**
 * Created by macbury on 19.05.15.
 */
public class LightRenderingSystem extends IteratingSystem implements ActionTimer.TimerListener {
  public static final float OCCULSION_TIMER_DELAY = 0.05f;
  private final TerrainEngine terrain;
  private final VoxelBatch batch;
  private final LevelEnv env;
  private final GameCamera camera;
  private final ActionTimer timer;
  private Array<Chunk> visibleChunks;
  private Array<VoxelChunkRenderable> visibleFaces;

  public LightRenderingSystem(Level level) {
    super(Family.getFor(ShadowComponent.class, RenderableComponent.class));
    visibleChunks = new Array<Chunk>();
    visibleFaces  = new Array<VoxelChunkRenderable>();

    this.timer   = new ActionTimer(OCCULSION_TIMER_DELAY, this);
    this.terrain = level.terrainEngine;
    this.batch   = level.colorBatch;
    this.env     = level.env;
    this.camera  = level.camera;
    timer.start();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

  }

  @Override
  public void update(float deltaTime) {
    timer.update(deltaTime);
    ForgE.fb.begin(FrameBufferManager.FRAMEBUFFER_SUN_DEPTH); {
      ForgE.graphics.clearAll(Color.BLACK);
      batch.begin(camera); {
        super.update(deltaTime);

        for (VoxelChunkRenderable renderable : visibleFaces) {
          if (renderable.mesh != null)
            batch.add(renderable);
        }

        batch.render(env);
      } batch.end();
    } ForgE.fb.end();
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    visibleChunks.clear();
    visibleFaces.clear();
    terrain.occulsion(camera, visibleChunks, visibleFaces);
  }
}
