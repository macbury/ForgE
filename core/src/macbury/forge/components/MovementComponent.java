package macbury.forge.components;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 20.10.14.
 */
public class MovementComponent extends BaseComponent {
  public final Vector3 direction       = new Vector3();
  public final Vector3 targetPosition = new Vector3();
  public float speed             = 0.0f;

  @Override
  public void reset() {
    speed = 0.0f;
    targetPosition.setZero();
    this.direction.setZero();
  }

  @Override
  public void set(BaseComponent otherComponent) {
    reset();
    MovementComponent otherMovement = (MovementComponent)otherComponent;
    speed                           = otherMovement.speed;
    direction.set(otherMovement.direction);
  }
}
