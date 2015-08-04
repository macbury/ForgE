package macbury.forge.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.octree.OctreeNode;
import macbury.forge.systems.engine.EntitySystemsManager;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.terrain.geometry.DynamicGeometryProvider;
import macbury.forge.terrain.geometry.TerrainGeometryProvider;
import macbury.forge.ui.DebugFrameBufferResult;
import macbury.forge.ui.FullScreenFrameBufferResult;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  public final EntitySystemsManager entities;
  public final GameCamera               camera;
  public final VoxelBatch               batch;
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
  public final LevelEnv env;
  public final Stage                    ui;
  public final TerrainGeometryProvider terrainGeometryProvider;

  public Level(LevelState state, TerrainGeometryProvider geometryProvider) {
    this.ui                       = new Stage(new ScreenViewport());
    this.env                      = state.env;
    this.state                    = state;
    this.terrainMap               = state.terrainMap;
    this.terrainGeometryProvider  = geometryProvider;
    this.renderContext            = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

    this.octree                   = OctreeNode.root();

    this.batch                    = new VoxelBatch(renderContext);
    this.camera                   = new GameCamera();
    this.frustrumDebugger         = new FrustrumDebugAndRenderer(camera);
    this.terrainEngine            = new TerrainEngine(this);
    this.entities                 = new EntitySystemsManager(this);

    octree.setBounds(terrainMap.getBounds(ChunkMap.TERRAIN_TILE_SIZE));

    ui.addActor(new FullScreenFrameBufferResult());

    //ui.addActor(DebugFrameBufferResult.build(Fbo.FRAMEBUFFER_REFLECTIONS, 256, 0, 0));
    //ui.addActor(DebugFrameBufferResult.build(Fbo.FRAMEBUFFER_REFRACTIONS, 256, 256, 0));
  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
    ui.getViewport().update(width, height, true);
  }

  public void render(float delta) {
    env.skybox.update(delta);
    batch.resetStats();
    camera.update();
    terrainEngine.update();
    entities.update(delta);

    ForgE.graphics.clearAll(Color.BLACK);

    ui.act(delta);
    ui.draw();
  }

  @Override
  public void dispose() {
    env.dispose();
    batch.dispose();
    terrainMap.dispose();
    entities.dispose();
    octree.dispose();
    frustrumDebugger.dispose();
    terrainEngine.dispose();
  }

}
