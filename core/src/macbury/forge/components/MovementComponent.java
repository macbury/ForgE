package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 20.10.14.
 */
public class MovementComponent extends BaseComponent {
  public Vector3 direction = new Vector3();

  public float speed       = 0.0f;

  @Override
  public void reset() {
    speed = 0.0f;
    this.direction.set(Vector3.Zero);
  }

  @Override
  public void set(BaseComponent otherComponent) {
    reset();
    MovementComponent otherMovement = (MovementComponent)otherComponent;
    speed                           = otherMovement.speed;
    direction.set(otherMovement.direction);
  }
}
