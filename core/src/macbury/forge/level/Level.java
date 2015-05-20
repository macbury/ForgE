package macbury.forge.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.FrameBufferManager;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.octree.OctreeNode;
import macbury.forge.shaders.providers.ColorShaderProvider;
import macbury.forge.shaders.providers.DepthShaderProvider;
import macbury.forge.systems.engine.EntitySystemsManager;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.ui.DebugFrameBufferResult;
import macbury.forge.ui.FullScreenFrameBufferResult;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  public final EntitySystemsManager entities;
  public final VoxelBatch colorBatch;
  public final ChunkMap                 terrainMap;
  public final LevelState               state;
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
  public final VoxelBatch               depthBatch;
  public final Stage                    ui;
  public final FPSLogger                fpsLogger;

  public Level(LevelState state) {
    this.fpsLogger           = new FPSLogger();
    this.env                 = state.env;
    this.state               = state;
    this.terrainMap          = state.terrainMap;
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.ui                  = new Stage(new ScreenViewport());

    this.octree              = OctreeNode.root();

    this.colorBatch          = new VoxelBatch(renderContext, new ColorShaderProvider());
    this.depthBatch          = new VoxelBatch(renderContext, new DepthShaderProvider());

    this.frustrumDebugger    = new FrustrumDebugAndRenderer(this.env.camera);
    this.terrainEngine       = new TerrainEngine(this);
    this.entities            = new EntitySystemsManager(this);

    octree.setBounds(terrainMap.getBounds(ChunkMap.TERRAIN_TILE_SIZE));

    ui.addActor(new FullScreenFrameBufferResult());

    DebugFrameBufferResult testColor = new DebugFrameBufferResult(FrameBufferManager.FRAMEBUFFER_SUN_DEPTH);
    testColor.setWidth(512);
    testColor.setHeight(512);
    ui.addActor(testColor);
  }

  public void resize(int width, int height) {
    env.camera.viewportWidth  = width;
    env.camera.viewportHeight = height;
    env.camera.update(true);
    ui.getViewport().update(width, height, true);
  }

  public void render(float delta) {
    fpsLogger.log();
    env.skybox.update(delta);
    colorBatch.resetStats();
    env.camera.update();
    terrainEngine.update();
    entities.update(delta);

    ForgE.graphics.clearAll(Color.BLACK);

    ui.act(delta);
    ui.draw();
  }

  @Override
  public void dispose() {
    env.dispose();
    colorBatch.dispose();
    terrainMap.dispose();
    entities.dispose();
    octree.dispose();
    frustrumDebugger.dispose();
    terrainEngine.dispose();
    depthBatch.dispose();
  }

}
