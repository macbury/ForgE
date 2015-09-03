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
  private final PlayerSystem player;
  //private final CollisionSystem collision;
  public final PhysicsSystem psychics;
  public final CharacterSystem character;

  public EntitySystemsManager(Level level) {
    psychics  = new PhysicsSystem();
    rendering = new WorldRenderingSystem(level);
    octree    = new OctreeSystem(level);
    debug     = new DebugSystem(level);
    character = new CharacterSystem();
    player    = new PlayerSystem();

    level.terrainEngine.addListener(psychics);

    addSystem(rendering);

    addSystem(octree);
    addSystem(player);
    addSystem(character);
    addSystem(psychics);

    addSystem(debug);

    addEntityListener(psychics);
    addEntityListener(debug);
    rendering.setListener(debug);
  }

  @Override
  public void dispose() {
    debug.dispose();
    psychics.dispose();
    removeAllEntities();
  }
}
