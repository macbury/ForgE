package macbury.forge.systems;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.Level;

/**
 * Created by macbury on 19.10.14.
 */
public class LevelEntityEngine extends PooledEngine implements Disposable {
  public final RenderingSystem rendering;
  public final TerrainSystem terrain;
  public final OctreeSystem octree;
  public final DebugSystem debug;
  public final MovementSystem movement;

  public LevelEntityEngine(Level level) {
    terrain   = new TerrainSystem(this);
    rendering = new RenderingSystem();
    octree    = new OctreeSystem(level.octree);
    debug     = new DebugSystem(level);
    movement  = new MovementSystem();

    addEntityListener(octree);

    addSystem(terrain);
    addSystem(movement);
    addSystem(octree);
    addSystem(debug);
    addSystem(rendering);
  }

  @Override
  public void dispose() {
    removeAllEntities();
    terrain.dispose();
  }
}
