package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import macbury.forge.ForgE;

/**
 * Created by macbury on 30.04.15.
 */
public class RigidBodyComoponent extends BulletPsychicsComponent {
  private btCollisionShape collisionShape;

  @Override
  public void initBullet(Matrix4 transform, btDiscreteDynamicsWorld world, Vector3 size, Entity entity) {
    RenderableComponent renderableComponent = entity.getComponent(RenderableComponent.class);
    this.collisionShape = renderableComponent.getAsset().getCollisionShape();
  }

  @Override
  public void disposeBullet() {

  }

  @Override
  public void set(BaseComponent otherComponent) {

  }
}
