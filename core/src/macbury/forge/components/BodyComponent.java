package macbury.forge.components;

import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 24.04.15.
 */
public class BodyComponent extends BaseComponent {
  public btCollisionShape collisionShape;
  public btRigidBody rigidBody;


  @Override
  public void set(BaseComponent otherComponent) {

  }

  @Override
  public void reset() {
    disposeBulletResources();
  }

  public void initBulletResources() {

  }

  public void disposeBulletResources() {
    if (collisionShape != null) {
      collisionShape.dispose();
    }
    collisionShape = null;
  }

  public class Constructor implements Disposable {


    @Override
    public void dispose() {

    }
  }
}
