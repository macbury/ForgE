package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.GdxRuntimeException;
import javafx.geometry.Pos;
import macbury.forge.ForgE;

/**
 * Created by macbury on 30.04.15.
 */
public class RigidBodyComoponent extends BulletPsychicsComponent {
  private btCollisionShape collisionShape;
  public Vector3 localInertia = new Vector3();
  public float mass           = 0f;
  private btRigidBody.btRigidBodyConstructionInfo constructionInfo;
  private btRigidBody rigidBody;
  private RigidBodyComponentMotionState motionState;
  private PositionComponent positionComponent;

  @Override
  public void initBullet(PositionComponent positionComponent, btDiscreteDynamicsWorld world, Vector3 size, Entity entity) {
    RenderableComponent renderableComponent = entity.getComponent(RenderableComponent.class);
    if (renderableComponent == null) {
      throw new GdxRuntimeException("Could not find renderable component for this entity: " + entity.toString());
    }


    this.collisionShape = renderableComponent.getAsset().getCollisionShape();
    if (mass > 0f)
      collisionShape.calculateLocalInertia(mass, localInertia);
    else
      localInertia.set(0, 0, 0);

    motionState = new RigidBodyComponentMotionState();
    motionState.setPositionComponent(positionComponent);
    constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, collisionShape, localInertia);
    this.rigidBody = new btRigidBody(constructionInfo);
    this.rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
    world.addRigidBody(rigidBody);
    constructionInfo.dispose();

  }

  @Override
  public void disposeBullet() {
    this.rigidBody.dispose();
    motionState.dispose();
  }

  @Override
  public void set(BaseComponent otherComponent) {
    RigidBodyComoponent otherRigidBody = (RigidBodyComoponent)otherComponent;
    this.localInertia.set(otherRigidBody.localInertia);
    this.mass = otherRigidBody.mass;
  }

  @Override
  public void reset() {
    super.reset();
    localInertia.setZero();
    mass  = 0;
  }

  static class RigidBodyComponentMotionState extends btMotionState {
    private PositionComponent positionComponent;

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
      positionComponent.getBulletMatrix(worldTrans);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
      positionComponent.setFromBulletMatrix(worldTrans);
    }

    public void setPositionComponent(PositionComponent positionComponent) {
      this.positionComponent = positionComponent;
    }

    @Override
    public void dispose() {
      super.dispose();
      positionComponent = null;
    }
  }
}
