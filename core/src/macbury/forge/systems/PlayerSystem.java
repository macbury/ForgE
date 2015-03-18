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
  private static final String TAG = "PlayerSystem";
  private ComponentMapper<PositionComponent> pm  = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm  = ComponentMapper.getFor(MovementComponent.class);
  private ComponentMapper<PlayerComponent> plm   = ComponentMapper.getFor(PlayerComponent.class);
  private int STRAFE_LEFT = Input.Keys.A;
  private int STRAFE_RIGHT = Input.Keys.D;
  private int FORWARD = Input.Keys.W;
  private int BACKWARD = Input.Keys.S;
  private float mouseSensitivity = 10f;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  public PlayerSystem() {
    super(Family.getFor(PlayerComponent.class, PositionComponent.class, MovementComponent.class));
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Camera camera                       = plm.get(entity).camera;
    MovementComponent movementComponent = mm.get(entity);
    PositionComponent positionComponent = pm.get(entity);

    if (camera != null) {
      float deltaX = -Gdx.input.getDeltaX() * mouseSensitivity * deltaTime;
      float deltaY = -Gdx.input.getDeltaY() * mouseSensitivity* deltaTime;

      camera.direction.rotate(camera.up, deltaX);
      tempA.set(camera.direction).crs(camera.up).nor();
      camera.direction.rotate(tempA, deltaY);

      //camera.direction.x = Math.max(camera.direction.x, -0.2f);
      //Gdx.app.log(TAG, camera.direction.toString());
      tempA.setZero();
      tempB.setZero();
      if (Gdx.input.isKeyPressed(FORWARD)) {
        tempA.set(camera.direction.x, 0, camera.direction.z);
      } else if (Gdx.input.isKeyPressed(BACKWARD)) {
        tempA.set(-camera.direction.x, 0, -camera.direction.z);
      }

      if (Gdx.input.isKeyPressed(STRAFE_LEFT)) {
        tempB.set(camera.direction).crs(camera.up).nor().scl(-1f);
      } else if (Gdx.input.isKeyPressed(STRAFE_RIGHT)) {
        tempB.set(camera.direction).crs(camera.up).nor();
      }

      movementComponent.direction.set(tempA.add(tempB).nor());
      camera.position.set(positionComponent.vector);
      //camera.direction.add(positionComponent.rotation.x, positionComponent.rotation.y, positionComponent.rotation.z);
    }
  }


}
