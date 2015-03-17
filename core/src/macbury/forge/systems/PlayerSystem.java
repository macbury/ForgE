package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PlayerComponent;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 17.03.15.
 */
public class PlayerSystem extends IteratingSystem {
  private ComponentMapper<PositionComponent> pm  = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm  = ComponentMapper.getFor(MovementComponent.class);
  private ComponentMapper<PlayerComponent> plm   = ComponentMapper.getFor(PlayerComponent.class);
  private int STRAFE_LEFT = Input.Keys.A;
  private int STRAFE_RIGHT = Input.Keys.D;
  private int FORWARD = Input.Keys.W;
  private int BACKWARD = Input.Keys.S;

  private Vector3 temp = new Vector3();
  public PlayerSystem() {
    super(Family.getFor(PlayerComponent.class, PositionComponent.class, MovementComponent.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Camera camera              = plm.get(entity).camera;
    MovementComponent movementComponent = mm.get(entity);
    PositionComponent positionComponent = pm.get(entity);
    if (camera != null) {
      if (Gdx.input.isKeyPressed(FORWARD)) {
        camera.direction.z = 1;
        movementComponent.direction.z = 1;
      } else if (Gdx.input.isKeyPressed(BACKWARD)) {
        camera.direction.z = -1;
        movementComponent.direction.z = -1;
      } else {
        movementComponent.direction.z = 0;
      }

      if (Gdx.input.isKeyPressed(STRAFE_LEFT)) {
        camera.direction.x = -1;
        movementComponent.direction.x = -1;
      } else if (Gdx.input.isKeyPressed(STRAFE_RIGHT)) {
        camera.direction.x = 1;
        movementComponent.direction.x= 1;
      } else {
        movementComponent.direction.x = 0;
      }

      camera.position.set(positionComponent.vector);
    }
  }

}
