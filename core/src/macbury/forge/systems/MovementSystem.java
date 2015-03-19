package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.components.GravityComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.level.Level;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 20.10.14.
 */
public class MovementSystem extends IteratingSystem {
  private final Level level;
  private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
  private ComponentMapper<GravityComponent>  gm = ComponentMapper.getFor(GravityComponent.class);

  private final Vector3 tempA;
  private final Vector3 tempB;
  private final Vector3i tempC;
  private BoundingBox tempBoundingBox = new BoundingBox();

  public MovementSystem(Level level) {
    super(Family.getFor(PositionComponent.class, MovementComponent.class));
    this.level = level;
    tempA = new Vector3();
    tempB = new Vector3();
    tempC = new Vector3i();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    MovementComponent movementComponent = mm.get(entity);
    PositionComponent positionComponent = pm.get(entity);
    GravityComponent  gravityComponent  = gm.get(entity);

    if (!movementComponent.direction.isZero()) {
      float distance = movementComponent.speed * deltaTime;
      tempA.set(
          movementComponent.direction.x * distance,
          movementComponent.direction.y * distance,
          movementComponent.direction.z * distance
      );
      tempB.set(positionComponent.vector).add(tempA);
      positionComponent.vector.set(tempB);
    }

    if (gravityComponent != null) {
      tempA.set(level.env.gravity).scl(deltaTime);
      tempB.set(positionComponent.vector).add(tempA);

      tempC.set(level.terrainMap.worldPositionToVoxelPosition(tempB));
      if (level.terrainMap.isEmpty(tempC)) {
        positionComponent.vector.set(tempB);
      }

      positionComponent.getBoundingBox(tempBoundingBox);

      //movementComponent.direction.sub()
    }

    //positionComponent.rotation.idt();
    //positionComponent.rotation.
  }

}
