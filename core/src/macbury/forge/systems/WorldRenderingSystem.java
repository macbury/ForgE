package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RenderableComponent;
import macbury.forge.graphics.camera.ICamera;
import macbury.forge.graphics.light.OrthographicDirectionalLight;
import macbury.forge.graphics.skybox.DayNightSkybox;
import macbury.forge.graphics.skybox.Skybox;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.Level;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.octree.query.FrustrumClassFilterOctreeQuery;
import macbury.forge.shaders.providers.DepthShaderProvider;
import macbury.forge.shaders.providers.ShaderProvider;
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
  private ShaderProvider colorShaderProvider;
  private DepthShaderProvider depthShaderProvider;
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
    this.colorShaderProvider    = level.colorShaderProvider;
    this.depthShaderProvider    = level.depthShaderProvider;
    this.env                    = level.env;
    this.mainCamera             = level.camera;
    this.skybox                 = level.env.skybox;
    this.octree                 = level.octree;
    this.finalBucket            = new Array<RenderableProvider>();
    frustrumOctreeQuery.setKlass(PositionComponent.class);
  }

  @Override
  public void update(float deltaTime) {
    env.depthShaderMode = LevelEnv.DepthShaderMode.Normal;
    batch.setShaderProvider(depthShaderProvider); {
      renderSunDepth();
    }

    batch.setShaderProvider(colorShaderProvider); {
      renderReflections();
      renderRefractions();

      env.water.clipMode = LevelEnv.ClipMode.None;
      prepareElementsToRender(mainCamera);
      renderFinal();
    }
    renderLightScattering();
  }

  private void renderSunDepth() {
    env.water.clipMode                    = LevelEnv.ClipMode.None;
    OrthographicDirectionalLight sunLight = env.mainLight;
/*
    sunLight.beginNear(mainCamera); {
      ForgE.fb.begin(Fbo.FRAMEBUFFER_SUN_NEAR_DEPTH); {
        renderBucketWith(false, false, sunLight.getShadowCamera());
      } ForgE.fb.end();
    } sunLight.endNear(mainCamera);

    sunLight.beginFar(mainCamera); {
      ForgE.fb.begin(Fbo.FRAMEBUFFER_SUN_FAR_DEPTH); {
        renderBucketWith(false, false, sunLight.getShadowCamera());
      } ForgE.fb.end();
    } sunLight.endFar(mainCamera);

 */
    sunLight.begin(mainCamera); {
      ForgE.fb.begin(Fbo.FRAMEBUFFER_SUN_FAR_DEPTH); {
        prepareElementsToRender(sunLight.getShadowCamera());
        renderBucketWith(false, false, sunLight.getShadowCamera());
      }
      ForgE.fb.end();
    } sunLight.end(mainCamera);
  }

  private void renderReflections() {
    env.water.clipMode      = LevelEnv.ClipMode.Reflection;
    //float cacheFar          = mainCamera.far;
    //mainCamera.far          = cacheFar / 2;
    float distance          = 2 * (mainCamera.position.y - env.water.getElevationWithWaterBlockHeight());
    mainCamera.position.y   -= distance;
    CameraUtils.invertPitch(mainCamera);
    mainCamera.update(true);
    ForgE.fb.begin(Fbo.FRAMEBUFFER_REFLECTIONS); {
      prepareElementsToRender(mainCamera);
      renderBucketWith(true, false, mainCamera);
    } ForgE.fb.end();

    mainCamera.position.y   += distance;
    CameraUtils.invertPitch(mainCamera);
    //mainCamera.far = cacheFar;
  }

  private void renderRefractions() {
    env.water.clipMode = LevelEnv.ClipMode.Refraction;
    ForgE.fb.begin(Fbo.FRAMEBUFFER_REFRACTIONS);
    {
      prepareElementsToRender(mainCamera);
      renderBucketWith(false, false, mainCamera);
    }
    ForgE.fb.end();
  }

  private void renderFinal() {
    ForgE.fb.begin(Fbo.FRAMEBUFFER_MAIN_COLOR); {
      renderBucketWith(true, true, mainCamera);
    } ForgE.fb.end();
  }

  private void renderLightScattering() {
    env.depthShaderMode = LevelEnv.DepthShaderMode.OnlyFront;
    ForgE.fb.begin(Fbo.FRAMEBUFFER_LIGHT_SCATTERING); {
      ForgE.graphics.clearAll(Color.CLEAR);
      batch.begin(mainCamera); {
        batch.setShaderProvider(colorShaderProvider); {
          if (DayNightSkybox.class.isInstance(skybox)) {
            DayNightSkybox dayNightSkybox = (DayNightSkybox)skybox;
            dayNightSkybox.setRenderOnlySun(true);
          }
          skybox.render(batch, env, mainCamera);
        }

        batch.setShaderProvider(depthShaderProvider); {
          batch.pushAll(terrain.visibleTerrainFaces);
          batch.addAll(finalBucket);
          batch.render(env);
        }
      } batch.end();
    } ForgE.fb.end();
  }

  private void prepareElementsToRender(ICamera camera) {
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
  }

  private void renderBucketWith(boolean withSkybox, boolean withWater, ICamera camera) {
    batch.begin((Camera) camera); {
      if (withSkybox) {
        ForgE.graphics.clearAll(env.skyColor);
        if (DayNightSkybox.class.isInstance(skybox)) {
          DayNightSkybox dayNightSkybox = (DayNightSkybox)skybox;
          dayNightSkybox.setRenderOnlySun(false);
        }
        skybox.render(batch, env, (Camera)camera);
      } else {
        ForgE.graphics.clearAll(Color.BLACK);
      }

      batch.pushAll(terrain.visibleTerrainFaces);
      if (withWater)
        batch.pushAll(terrain.visibleWaterFaces);
      batch.addAll(finalBucket);
      batch.render(env);
    } batch.end();
  }

}
