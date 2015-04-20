package macbury.forge.systems.engine;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.components.PositionComponent;
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
  //private final CollisionSystem collision;
  public final PositionSystem positions;
  public final PsychicsSystem psychics;

  public LevelEntityEngine(Level level) {
    psychics  = new PsychicsSystem();
    rendering = new RenderingSystem(level);
    octree    = new OctreeSystem(level);
    debug     = new DebugSystem(level);
    movement  = new MovementSystem(level);
    culling   = new CullingSystem(level);
    player    = new PlayerSystem();
    positions = new PositionSystem();
    //collision = new CollisionSystem(level.terrainMap, level.octree);

    addSystem(culling);
    addSystem(player);
    addSystem(movement);

    addSystem(octree);

    addSystem(psychics);
    addSystem(positions);

    addSystem(debug);
    addSystem(rendering);

    addEntityListener(psychics);
  }

  @Override
  public void dispose() {

    debug.dispose();
    psychics.dispose();
    removeAllEntities();

  }
}
