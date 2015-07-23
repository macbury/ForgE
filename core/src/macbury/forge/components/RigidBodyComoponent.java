package macbury.forge.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.systems.PhysicsSystem;

/**
 * Created by macbury on 30.04.15.
 */
public class RigidBodyComoponent extends BulletPsychicsComponent {
  public Vector3 localInertia = new Vector3();
  public Vector3 initialImpulse = new Vector3();
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

    if (shape == null) {
      throw new GdxRuntimeException("Could not find shape for this component: " + this.getClass().toString());
    }

    this.collisionShape = shape.build();
    if (mass > 0f)
      collisionShape.calculateLocalInertia(mass, localInertia);
    else
      localInertia.set(0, 0, 0);

    motionState      = new RigidBodyComponentMotionState();
    motionState.setPositionComponent(positionComponent);
    constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, collisionShape, localInertia);
    this.rigidBody   = new btRigidBody(constructionInfo);

    //this.rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
    if (!initialImpulse.isZero()) {
      rigidBody.applyCentralImpulse(initialImpulse);
    }

    world.addRigidBody(
        rigidBody,
        PhysicsSystem.Flags.Object.mask,
        PhysicsSystem.Flags.All.mask
    );
    constructionInfo.dispose();
  }

  @Override
  public void disposeBullet() {
    this.rigidBody.dispose();
    motionState.dispose();
  }

  @Override
  public void set(BaseComponent otherComponent) {
    super.set(otherComponent);
    RigidBodyComoponent otherRigidBody = (RigidBodyComoponent)otherComponent;
    this.localInertia.set(otherRigidBody.localInertia);
    this.initialImpulse.set(otherRigidBody.initialImpulse);
    this.mass = otherRigidBody.mass;
  }

  @Override
  public void reset() {
    super.reset();
    localInertia.setZero();
    initialImpulse.setZero();
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
