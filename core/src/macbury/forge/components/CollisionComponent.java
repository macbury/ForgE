package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by macbury on 21.03.15.
 */
public class CollisionComponent extends BaseComponent {
  public final Vector3 collisionPositionChanges = new Vector3();
  public boolean solid          = false;
  private int collisionPositionChangeCount;

  @Override
  public void reset() {
    collisionPositionChanges.setZero();
    collisionPositionChangeCount = 0;
    solid = false;
  }

  @Override
  public void set(BaseComponent otherComponent) {
    solid = ((CollisionComponent)otherComponent).solid;
  }

  public void addCollisionPositionChange(Vector3 deltaVec) {
    collisionPositionChangeCount++;
    collisionPositionChanges.add(deltaVec);
  }

  public void handleCollisions(Vector3 adjustedPositionDeltaVecOut) {
    if (collisionPositionChangeCount > 0) {
      collisionPositionChanges.scl(1f / collisionPositionChangeCount);
      collisionPositionChanges.scl(-1f);
      adjustedPositionDeltaVecOut.set(collisionPositionChanges);
      collisionPositionChangeCount = 0;
      collisionPositionChanges.setZero();
    } else {
      adjustedPositionDeltaVecOut.setZero();
    }
  }

}
