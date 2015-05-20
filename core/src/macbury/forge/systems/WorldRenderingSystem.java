package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RenderableComponent;
import macbury.forge.graphics.Skybox;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.FrameBufferManager;
import macbury.forge.level.Level;
import macbury.forge.level.LevelEnv;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.utils.ActionTimer;
import macbury.forge.utils.OcculsionTimer;

/**
 * Created by macbury on 19.10.14.
 */
public class WorldRenderingSystem extends IteratingSystem implements ActionTimer.TimerListener {
  private final GameCamera camera;
  private final LevelEnv env;
  private final TerrainEngine terrain;
  private final Skybox skybox;
  private final ActionTimer timer;
  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<RenderableComponent> rm = ComponentMapper.getFor(RenderableComponent.class);
  private VoxelBatch batch;
  private Array<Chunk> visibleChunks               = new Array<Chunk>();
  private Array<VoxelChunkRenderable> visibleFaces = new Array<VoxelChunkRenderable>();

  public WorldRenderingSystem(Level level) {
    super(Family.getFor(PositionComponent.class, RenderableComponent.class));

    this.terrain = level.terrainEngine;
    this.batch   = level.colorBatch;
    this.env     = level.env;
    this.camera  = level.env.camera;
    this.skybox  = level.env.skybox;
    this.timer   = new OcculsionTimer(this);
    timer.start();
  }

  @Override
  public void update(float deltaTime) {
    timer.update(deltaTime);
    ForgE.fb.begin(FrameBufferManager.FRAMEBUFFER_MAIN_COLOR); {
      ForgE.graphics.clearAll(env.skyColor);

      batch.begin(camera); {
        batch.add(skybox);
        batch.render(env);

        for (VoxelChunkRenderable renderable : visibleFaces) {
          if (renderable.mesh != null)
            batch.add(renderable);
        }

        super.update(deltaTime);

        batch.render(env);
      } batch.end();
    } ForgE.fb.end();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    PositionComponent position     = pm.get(entity);
    RenderableComponent renderable = rm.get(entity);

    if (position.visible) {
      ModelInstance modelInstance = renderable.getModelInstance();

      position.applyWorldTransform(modelInstance.transform);
      batch.add(modelInstance);
    }
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    terrain.occulsion(camera, visibleChunks, visibleFaces);
  }
}
