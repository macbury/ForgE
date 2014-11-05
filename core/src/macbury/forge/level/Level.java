package macbury.forge.level;

import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.systems.engine.LevelEntityEngine;
import macbury.forge.terrain.TerrainEngine;

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
  public final LevelEnv env;

  public Level(LevelState state) {
    this.env                 = new LevelEnv();
    this.state               = state;
    this.terrainMap          = state.terrainMap;
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

    this.octree              = OctreeNode.root();

    this.batch               = new VoxelBatch(renderContext);
    this.camera              = new GameCamera();
    this.frustrumDebugger    = new FrustrumDebugAndRenderer(camera);
    this.terrainEngine       = new TerrainEngine(this);
    this.entities            = new LevelEntityEngine(this);

    octree.setBounds(terrainMap.getBounds(ChunkMap.TERRAIN_TILE_SIZE));
/*
    for (int i = 0; i < 50; i++) {
      Entity e          = entities.createEntity();
      Position position = entities.createComponent(Position.class);
      position.vector.set(5,2,100+i);
      position.size.set(1,1,1);
      Movement movement = entities.createComponent(Movement.class);
      movement.speed    = 40;
      movement.direction.set(Vector3.X);

      e.add(movement);
      e.add(position);
      entities.addEntity(e);
    }*/
  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  public void render(float delta) {
    camera.update();
    terrainEngine.update();

    ForgE.graphics.clearAll(env.skyColor);
    batch.begin(camera); {
      entities.update(delta);
      batch.add(terrainEngine);
      batch.render(env);
    } batch.end();
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
