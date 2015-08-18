package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RenderableComponent;
import macbury.forge.graphics.camera.ICamera;
import macbury.forge.graphics.light.OrthographicDirectionalLight;
import macbury.forge.graphics.skybox.Skybox;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.Level;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.octree.query.FrustrumClassFilterOctreeQuery;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.utils.CameraUtils;

/**
 * Created by macbury on 19.10.14.
 */
public class WorldRenderingSystem extends EntitySystem {
  private final GameCamera mainCamera;
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
    this.mainCamera             = level.camera;
    this.skybox                 = level.env.skybox;
    this.octree                 = level.octree;
    this.finalBucket            = new Array<RenderableProvider>();
    frustrumOctreeQuery.setKlass(PositionComponent.class);
  }

  @Override
  public void update(float deltaTime) {
    renderSunDepth();
    renderReflections();
    renderRefractions();

    renderFinal();
  }

  private void renderSunDepth() {
    env.water.clipMode                    = LevelEnv.ClipMode.None;
    OrthographicDirectionalLight sunLight = env.mainLight;
    sunLight.update(mainCamera);
    ForgE.fb.begin(Fbo.FRAMEBUFFER_SUN_DEPTH); {
      renderBucketWith(false, false, sunLight.getShadowCamera());
    } ForgE.fb.end();
  }

  private void renderReflections() {
    env.water.clipMode      = LevelEnv.ClipMode.Reflection;
    float cacheFar          = mainCamera.far;
    mainCamera.far          = cacheFar / 2;
    float distance          = 2 * (mainCamera.position.y - env.water.getElevationWithWaterBlockHeight());
    mainCamera.position.y   -= distance;
    CameraUtils.invertPitch(mainCamera);
    mainCamera.update(true);
    ForgE.fb.begin(Fbo.FRAMEBUFFER_REFLECTIONS); {
      renderBucketWith(true, false, mainCamera);
    } ForgE.fb.end();

    mainCamera.position.y   += distance;
    CameraUtils.invertPitch(mainCamera);
    mainCamera.far = cacheFar;
  }

  private void renderRefractions() {
    env.water.clipMode = LevelEnv.ClipMode.Refraction;
    ForgE.fb.begin(Fbo.FRAMEBUFFER_REFRACTIONS); {
      renderBucketWith(false, false, mainCamera);
    } ForgE.fb.end();
  }

  private void renderFinal() {
    env.water.clipMode = LevelEnv.ClipMode.None;
    ForgE.fb.begin(Fbo.FRAMEBUFFER_MAIN_COLOR); {
      renderBucketWith(true, true, mainCamera);
    } ForgE.fb.end();
  }

  private void renderBucketWith(boolean withSkybox, boolean withWater, ICamera camera) {
    finalBucket.clear();
    octreeVisibleObjects.clear();
    camera.extendFov(); {
      terrain.occulsion(camera);//TODO: change!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      frustrumOctreeQuery.setFrustum(camera.normalOrDebugFrustrum());//TODO: change!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

    batch.begin((Camera)camera); {
      ForgE.graphics.clearAll(env.skyColor);
      if (withSkybox){
        skybox.render(batch, env, (Camera)camera);
      }

      batch.pushAll(terrain.visibleTerrainFaces);
      if (withWater)
        batch.pushAll(terrain.visibleWaterFaces);
      batch.addAll(finalBucket);
      batch.render(env);
    } batch.end();
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
