package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RenderableComponent;
import macbury.forge.graphics.Skybox;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.Level;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.uniforms.UniformClipWaterPlane;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.utils.CameraUtils;

/**
 * Created by macbury on 19.10.14.
 */
public class WorldRenderingSystem extends IteratingSystem {
  private final GameCamera camera;
  private final LevelEnv env;
  private final TerrainEngine terrain;
  private final Skybox skybox;


  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<RenderableComponent> rm = ComponentMapper.getFor(RenderableComponent.class);
  private VoxelBatch batch;
  private final Array<RenderableProvider> finalBucket;
  private final Array<RenderableProvider> reflectionBucket;
  private final Array<RenderableProvider> refractionBucket;
  private Vector3 tempNormal = new Vector3();

  public WorldRenderingSystem(Level level) {
    super(Family.getFor(PositionComponent.class, RenderableComponent.class));

    this.terrain                = level.terrainEngine;
    this.batch                  = level.batch;
    this.env                    = level.env;
    this.camera                 = level.camera;
    this.skybox                 = level.env.skybox;
    this.finalBucket            = new Array<RenderableProvider>();
    this.reflectionBucket       = new Array<RenderableProvider>();
    this.refractionBucket       = new Array<RenderableProvider>();
  }

  @Override
  public void update(float deltaTime) {
    finalBucket.clear();
    reflectionBucket.clear();
    refractionBucket.clear();
    super.update(deltaTime);

    renderReflections();
    renderRefractions();

    renderFinal();
  }

  private void renderReflections() {
    env.water.clipMode  = LevelEnv.ClipMode.Reflection;
    float distance      = 2 * (camera.position.y - env.water.getElevationWithWaterBlockHeight());
    camera.position.y   -= distance;
    CameraUtils.invertPitch(camera);

    renderBucketWith(finalBucket, true, false, Fbo.FRAMEBUFFER_REFLECTIONS);
    camera.position.y   += distance;
    CameraUtils.invertPitch(camera);
  }

  private void renderRefractions() {
    env.water.clipMode = LevelEnv.ClipMode.Refraction;
    renderBucketWith(finalBucket, false, false, Fbo.FRAMEBUFFER_REFRACTIONS);
  }

  private void renderFinal() {
    env.water.clipMode = LevelEnv.ClipMode.None;
    renderBucketWith(finalBucket, true, true, Fbo.FRAMEBUFFER_MAIN_COLOR);
  }

  private void renderBucketWith(Array<RenderableProvider> bucket, boolean withSkybox, boolean withWater, String fbo) {
    ForgE.fb.begin(fbo); {
      batch.begin(camera); {
        ForgE.graphics.clearAll(env.skyColor);
        if (withSkybox){
          batch.add(skybox);
          batch.render(env);
        }

        batch.pushAll(terrain.visibleTerrainFaces);
        if (withWater)
          batch.pushAll(terrain.visibleWaterFaces);
        batch.addAll(bucket);
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
      finalBucket.add(modelInstance);
    }
  }
}
