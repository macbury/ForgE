package macbury.forge.systems.engine;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.Level;
import macbury.forge.systems.*;

/**
 * Created by macbury on 19.10.14.
 */
public class LevelEntityEngine extends PooledEngine implements Disposable {
  public final RenderingSystem rendering;
  public final OctreeSystem octree;
  public final DebugSystem debug;
  public final MovementSystem movement;
  public final CullingSystem culling;
  private final PlayerSystem player;

  public LevelEntityEngine(Level level) {
    rendering = new RenderingSystem(level);
    octree    = new OctreeSystem(level);
    debug     = new DebugSystem(level);
    movement  = new MovementSystem();
    culling   = new CullingSystem(level);
    player    = new PlayerSystem();

    addSystem(player);
    addSystem(movement);
    addSystem(octree);
    addSystem(culling);
    addSystem(debug);
    addSystem(rendering);
  }

  @Override
  public void dispose() {
    removeAllEntities();
  }
}
