package macbury.forge.systems.engine;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.Level;
import macbury.forge.systems.*;

/**
 * Created by macbury on 19.10.14.
 */
public class EntitySystemsManager extends PooledEngine implements Disposable {
  public final WorldRenderingSystem rendering;
  public final OctreeSystem octree;
  public final DebugSystem debug;
  public final CullingSystem culling;
  private final PlayerSystem player;
  //private final CollisionSystem collision;
  public final PsychicsSystem psychics;
  public final PositionSystem position;
  public final CharacterSystem character;

  public EntitySystemsManager(Level level) {
    psychics  = new PsychicsSystem();
    rendering = new WorldRenderingSystem(level);
    position  = new PositionSystem();
    octree    = new OctreeSystem(level);
    debug     = new DebugSystem(level);
    culling   = new CullingSystem(level);
    character = new CharacterSystem();
    player    = new PlayerSystem();

    addSystem(culling);
    addSystem(player);

    addSystem(octree);

    addSystem(character);
    addSystem(psychics);

    addSystem(position);

    addSystem(rendering);
    addSystem(debug);

    addEntityListener(psychics);
  }

  @Override
  public void dispose() {
    debug.dispose();
    psychics.dispose();
    removeAllEntities();
  }
}
