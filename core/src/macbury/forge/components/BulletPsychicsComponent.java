package macbury.forge.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

/**
 * Created by macbury on 24.04.15.
 */
public abstract class BulletPsychicsComponent extends BaseComponent {
  protected btDiscreteDynamicsWorld world;
  public BulletShapeBuilder shape;
  protected btCollisionShape collisionShape;
  protected static final Matrix4 tempMat = new Matrix4();
  public abstract void initBullet(PositionComponent positionComponent, btDiscreteDynamicsWorld world, Vector3 size, Entity entity);
  public abstract void disposeBullet();

  @Override
  public void set(BaseComponent otherComponent) {
    BulletPsychicsComponent bulletPsychicsComponent = (BulletPsychicsComponent)otherComponent;
    this.shape = bulletPsychicsComponent.shape;
  }

  @Override
  public void reset() {
    disposeBullet();
    if (collisionShape == null)
      collisionShape.dispose();
    collisionShape = null;
    shape = null;
  }

}
