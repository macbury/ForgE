package macbury.forge.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import macbury.forge.components.CollisionComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 21.03.15.
 */
public class CollisionSystem extends IteratingSystem {
  public CollisionSystem() {
    super(Family.getFor(MovementComponent.class, PositionComponent.class, CollisionComponent.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    //TODO: check collision with terrain
    //TODO: check collision with entities
  }
}
