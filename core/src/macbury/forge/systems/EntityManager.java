package macbury.forge.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.components.PlayerComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.systems.engine.LevelEntityEngine;

/**
 * Created by macbury on 19.03.15.
 */
public class EntityManager implements Disposable {
  @Override
  public void dispose() {

  }

  public Entity build(String entityId, LevelEntityEngine entityEngine) {
    return null;
  }

  public Entity player(Vector3 spawnPosition, PerspectiveCamera camera, LevelEntityEngine entityEngine) {
    Entity player = build("player", entityEngine);
    player.getComponent(PositionComponent.class).vector.set(spawnPosition);
    player.getComponent(PlayerComponent.class).camera = camera;
    return player;
  }
}
