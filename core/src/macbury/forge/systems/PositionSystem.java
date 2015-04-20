package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import macbury.forge.components.CollisionComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 20.04.15.
 */
public class PositionSystem extends IteratingSystem {
  private ComponentMapper<PositionComponent> pm   = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm   = ComponentMapper.getFor(MovementComponent.class);

  public PositionSystem() {
    super(Family.getFor(PositionComponent.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent pc = pm.get(entity);
    MovementComponent mc = mm.get(entity);

    if (mc != null) {
      pc.vector.add(mc.targetPosition);
      pc.updateTransformMatrix();
    }
  }
}
