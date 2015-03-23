package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by macbury on 21.03.15.
 */
public class CollisionComponent extends BaseComponent {
  public final Vector3 position = new Vector3();
  @Override
  public void reset() {
    position.setZero();
  }

  @Override
  public void set(BaseComponent otherComponent) {

  }
}
