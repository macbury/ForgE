package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
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
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.octree.query.FrustrumClassFilterOctreeQuery;
import macbury.forge.shaders.uniforms.UniformClipWaterPlane;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.utils.CameraUtils;

/**
 * Created by macbury on 19.10.14.
 */
public class WorldRenderingSystem extends EntitySystem {
  private final GameCamera camera;
  private final LevelEnv env;
  private final TerrainEngine terrain;
  private final Skybox skybox;
  private final OctreeNode octree;

  private final FrustrumClassFilterOctreeQuery frustrumOctreeQuery = new FrustrumClassFilterOctreeQuery();
  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<RenderableComponent> rm = ComponentMapper.getFor(RenderableComponent.class);
  private VoxelBatch batch;
  private final Array<OctreeObject> octreeVisibleObjects = new Array<OctreeObject>();
  private final Array<RenderableProvider> finalBucket;
  private Vector3 tempNormal = new Vector3();

  public WorldRenderingSystem(Level level) {
    super();

    this.terrain                = level.terrainEngine;
    this.batch                  = level.batch;
    this.env                    = level.env;
    this.camera                 = level.camera;
    this.skybox                 = level.env.skybox;
    this.octree                 = level.octree;
    this.finalBucket            = new Array<RenderableProvider>();
    frustrumOctreeQuery.setKlass(PositionComponent.class);
  }

  @Override
  public void update(float deltaTime) {
    renderReflections();
    renderRefractions();

    renderFinal();
  }

  private void renderReflections() {
    env.water.clipMode  = LevelEnv.ClipMode.Reflection;
    float distance      = 2 * (camera.position.y - env.water.getElevationWithWaterBlockHeight());
    camera.position.y   -= distance;
    CameraUtils.invertPitch(camera);

    renderBucketWith(true, false, Fbo.FRAMEBUFFER_REFLECTIONS);
    camera.position.y   += distance;
    CameraUtils.invertPitch(camera);
  }

  private void renderRefractions() {
    env.water.clipMode = LevelEnv.ClipMode.Refraction;
    renderBucketWith(true, false, Fbo.FRAMEBUFFER_REFRACTIONS);
  }

  private void renderFinal() {
    env.water.clipMode = LevelEnv.ClipMode.None;
    renderBucketWith(true, true, Fbo.FRAMEBUFFER_MAIN_COLOR);
  }

  private void renderBucketWith(boolean withSkybox, boolean withWater, String fbo) {
    ForgE.fb.begin(fbo); {
      finalBucket.clear();
      octreeVisibleObjects.clear();
      camera.extendFov(); {
        terrain.occulsion(camera);
        frustrumOctreeQuery.setFrustum(camera.normalOrDebugFrustrum());
        octree.retrieve(octreeVisibleObjects, frustrumOctreeQuery);
      } camera.restoreFov();

      for (int i = 0; i < octreeVisibleObjects.size; i++) {
        PositionComponent position = (PositionComponent) octreeVisibleObjects.get(i);
        if (position.entity != null && rm.has(position.entity)) {
          RenderableComponent renderable = rm.get(position.entity);
          ModelInstance modelInstance    = renderable.getModelInstance();

          position.applyWorldTransform(modelInstance.transform);
          finalBucket.add(modelInstance);
        }
      }

      batch.begin(camera); {
        ForgE.graphics.clearAll(env.skyColor);
        if (withSkybox){
          batch.add(skybox);
          batch.render(env);
        }

        batch.pushAll(terrain.visibleTerrainFaces);
        if (withWater)
          batch.pushAll(terrain.visibleWaterFaces);
        batch.addAll(finalBucket);
        batch.render(env);
      } batch.end();
    } ForgE.fb.end();
  }

  public void processEntity(Entity entity, float deltaTime) {
    PositionComponent position     = pm.get(entity);
    RenderableComponent renderable = rm.get(entity);
/*
    if (position.visible) {
      ModelInstance modelInstance = renderable.getModelInstance();

      position.applyWorldTransform(modelInstance.transform);
      finalBucket.add(modelInstance);
    }*/
  }
}
