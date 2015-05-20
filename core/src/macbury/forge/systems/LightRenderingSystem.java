package macbury.forge.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
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
import macbury.forge.utils.OcculsionTimer;

/**
 * Created by macbury on 19.05.15.
 */
public class LightRenderingSystem extends IteratingSystem implements ActionTimer.TimerListener {
  private static final String SHADER_LIGHTMAP = "lightmap";
  private final TerrainEngine terrain;
  private final VoxelBatch batch;
  private final LevelEnv env;
  private final GameCamera mainCamera;
  private final ActionTimer timer;
  private final RenderContext renderContext;
  private Array<Chunk> visibleChunks;
  private Array<VoxelChunkRenderable> visibleFaces;
  private Vector3 tempVec = new Vector3();
  public LightRenderingSystem(Level level) {
    super(Family.getFor(ShadowComponent.class, RenderableComponent.class));
    visibleChunks = new Array<Chunk>();
    visibleFaces  = new Array<VoxelChunkRenderable>();

    this.timer      = new OcculsionTimer(this);
    this.terrain    = level.terrainEngine;
    this.batch      = level.depthBatch;
    this.env        = level.env;
    this.mainCamera = level.camera;
    this.renderContext = level.renderContext;
    timer.start();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

  }

  @Override
  public void update(float deltaTime) {
    timer.update(deltaTime);
    updateLightPosition();
    renderDepthMap(deltaTime);
    renderLightMap();
  }

  private void renderLightMap() {
    ForgE.fb.renderFB(renderContext, FrameBufferManager.FRAMEBUFFER_LIGHT_MAP, SHADER_LIGHTMAP);
  }

  private void renderDepthMap(float deltaTime) {
    ForgE.fb.begin(FrameBufferManager.FRAMEBUFFER_SUN_DEPTH); {
      ForgE.graphics.clearAll(Color.BLACK);
      batch.begin(env.getMainLight()); {
        super.update(deltaTime);

        for (VoxelChunkRenderable renderable : visibleFaces) {
          if (renderable.mesh != null)
            batch.add(renderable);
        }

        batch.render(env);
      } batch.end();
    } ForgE.fb.end();
  }

  private void updateLightPosition() {
    tempVec.set(mainCamera.position).add(1,10,1);
    env.mainLight.position.set(tempVec);
    env.mainLight.lookAt(mainCamera.position);
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    terrain.occulsion(env.getMainLight(), visibleChunks, visibleFaces);
  }
}
