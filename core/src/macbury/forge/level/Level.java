package macbury.forge.level;

import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.octree.OctreeNode;
import macbury.forge.systems.engine.LevelEntityEngine;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  public final LevelEntityEngine        entities;
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
  public final LevelEnv                 env;

  public Level(LevelState state) {
    this.env                 = state.env;
    this.state               = state;
    this.terrainMap          = state.terrainMap;
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

    this.octree              = OctreeNode.root();

    this.batch               = new VoxelBatch(renderContext);
    this.camera              = new GameCamera();
    this.frustrumDebugger    = new FrustrumDebugAndRenderer(camera);
    this.terrainEngine       = new TerrainEngine(this);
    this.entities            = new LevelEntityEngine(this);

    //this.psychics            = new BulletWorld(collisionConfiguration, dispatcher, sweep, solver, collisionWorld);

    octree.setBounds(terrainMap.getBounds(ChunkMap.TERRAIN_TILE_SIZE));

  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  public void render(float delta) {
    batch.resetStats();
    camera.update();
    terrainEngine.update();
    entities.update(delta);

  }


  @Override
  public void dispose() {
    batch.dispose();
    terrainMap.dispose();
    entities.dispose();
    octree.dispose();
    frustrumDebugger.dispose();
    terrainEngine.dispose();
  }

}
