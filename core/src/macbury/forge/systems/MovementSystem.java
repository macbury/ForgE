package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
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
  private final Vector3 tempB;

  public MovementSystem() {
    super(Family.getFor(PositionComponent.class, MovementComponent.class));
    tempA = new Vector3();
    tempB = new Vector3();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    MovementComponent movementComponent = mm.get(entity);
    PositionComponent positionComponent = pm.get(entity);

    float distance = movementComponent.speed * deltaTime;
    tempA.set(
        movementComponent.direction.x * distance,
        movementComponent.direction.y * distance,
        movementComponent.direction.z * distance
    );
    positionComponent.vector.add(tempA);
  }

}
