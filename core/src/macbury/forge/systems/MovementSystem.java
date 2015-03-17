package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 20.10.14.
 */
public class MovementSystem extends IteratingSystem {
  private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
  private final Vector3 tempA;

  public MovementSystem() {
    super(Family.getFor(PositionComponent.class, MovementComponent.class));
    tempA = new Vector3();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    MovementComponent movementComponent = mm.get(entity);
    PositionComponent positionComponent = pm.get(entity);
    tempA.set(
      movementComponent.direction.x * movementComponent.speed * deltaTime,
      movementComponent.direction.y * movementComponent.speed * deltaTime,
      movementComponent.direction.z * movementComponent.speed * deltaTime
    );

    if (positionComponent.vector.x < 10) {
      movementComponent.direction.x = 1;
    } else if (positionComponent.vector.x > 200) {
      movementComponent.direction.x = -1;
    }

    positionComponent.vector.add(tempA);
  }

}
