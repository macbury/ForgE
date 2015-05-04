package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 24.04.15.
 */
public class PositionSystem extends IteratingSystem {
  private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

  public PositionSystem() {
    super(Family.getFor(PositionComponent.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    //PositionComponent positionComponent = pm.get(entity);
   // positionComponent.updateTransformMatrix();
  }
}
