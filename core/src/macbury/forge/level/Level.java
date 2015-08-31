package macbury.forge.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.octree.OctreeNode;
import macbury.forge.shaders.providers.DepthShaderProvider;
import macbury.forge.shaders.providers.ShaderProvider;
import macbury.forge.systems.engine.EntitySystemsManager;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.terrain.geometry.TerrainGeometryProvider;
import macbury.forge.ui.DebugFrameBufferResult;
import macbury.forge.ui.FullScreenFrameBufferResult;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  private static final String TAG = "Level";
  public final EntitySystemsManager entities;
  public final GameCamera               camera;
  public final VoxelBatch               batch;
  public final ShaderProvider           colorShaderProvider;
  public final DepthShaderProvider depthShaderProvider;
  public ChunkMap                 terrainMap;
  public LevelState               state;
  /**
   * Dynamic octree refreshed every 100 ms
   */
  public final OctreeNode octree;
  /**
   * Static octree refreshed only after terrain was rebuild
   */
  public final RenderContext            renderContext;
  public final FrustrumDebugAndRenderer frustrumDebugger;
  public final TerrainEngine            terrainEngine;
  public final LevelEnv                 env;
  public final Stage                    ui;
  public PostProcessingManager    postProcessing;
  public final TerrainGeometryProvider terrainGeometryProvider;

  public Level(LevelState state, TerrainGeometryProvider geometryProvider) {
    this.colorShaderProvider      = new ShaderProvider();
    this.depthShaderProvider      = new DepthShaderProvider();
    this.ui                       = new Stage(new ScreenViewport());
    this.env                      = state.env;
    this.state                    = state;
    this.terrainMap               = state.terrainMap;
    this.terrainGeometryProvider  = geometryProvider;
    this.postProcessing           = new PostProcessingManager(ForgE.files.internal("postprocessing:default.json"));
    this.renderContext            = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

    this.octree                   = OctreeNode.root();

    this.batch                    = new VoxelBatch(renderContext, colorShaderProvider);
    this.camera                   = env.mainCamera;
    this.frustrumDebugger         = new FrustrumDebugAndRenderer();
    frustrumDebugger.add(camera);
    frustrumDebugger.add(env.mainLight.getShadowCamera());
    this.terrainEngine            = new TerrainEngine(this);
    this.entities                 = new EntitySystemsManager(this);

    octree.setBounds(terrainMap.getBounds(ChunkMap.TERRAIN_TILE_SIZE));
    ui.addActor(new FullScreenFrameBufferResult(Fbo.FRAMEBUFFER_FINAL));

    /*Array<String> fbos = ForgE.fb.all().keys().toArray();
    int size = Gdx.graphics.getWidth() / (fbos.size + 1);

    for (int i = 0; i < fbos.size; i++) {
      ui.addActor(DebugFrameBufferResult.build(fbos.get(i), size, i * size, 0));
    }*/

    //ui.addActor(DebugFrameBufferResult.build(Fbo.FRAMEBUFFER_SUN_FAR_DEPTH, 256, 0, 0));
    //ui.addActor(DebugFrameBufferResult.build(Fbo.FRAMEBUFFER_SUN_NEAR_DEPTH, 256, 256, 0));
   // ui.addActor(DebugFrameBufferResult.build(Fbo.FRAMEBUFFER_REFLECTIONS, 256, 0, 0));
    //ui.addActor(DebugFrameBufferResult.build(Fbo.FRAMEBUFFER_REFRACTIONS, 256, 256, 0));
  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
    ui.getViewport().update(width, height, true);
    postProcessing.reload();
  }

  public void render(float delta) {
    batch.resetStats();
    camera.update();
    env.skybox.update(delta, camera);

    terrainEngine.update();
    entities.update(delta);

    postProcessing.render(renderContext, env);

    ForgE.graphics.clearAll(Color.BLACK);

    ui.act(delta);
    ui.draw();
  }

  @Override
  public void dispose() {
    Gdx.app.log(TAG, "Dispose...");
    env.dispose();
    batch.dispose();
    terrainMap.dispose();
    entities.dispose();
    octree.dispose();
    frustrumDebugger.dispose();
    terrainEngine.dispose();
    terrainGeometryProvider.dispose();
    state.dispose();
    state = null;
    terrainMap = null;
    colorShaderProvider.dispose();
    depthShaderProvider.dispose();
    postProcessing.dispose();
    ui.dispose();
  }

}
