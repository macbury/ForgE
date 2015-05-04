package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;

/**
 * Created by macbury on 24.04.15.
 */
public class CharacterComponent extends BulletPsychicsComponent {
  private final static float DEFAULT_STEP_HEIGHT = 0.85f;
  private static final float DEFAULT_JUMP_SPEED = 0.1F;
  private static final float DEFAULT_MAX_JUMP_HEIGHT = 2;
  private static final float DEFAULT_SLOPE = 0.78f;
  public btPairCachingGhostObject ghostObject;
  private btCapsuleShape ghostShape;
  public btKinematicCharacterController characterController;
  public Vector3 speed        = new Vector3();
  private float stepHeight = DEFAULT_STEP_HEIGHT;
  public float jumpSpeed;
  public float maxJumpHeight;
  public float maxSlope;

  @Override
  public void initBullet(PositionComponent positionComponent, btDiscreteDynamicsWorld world, Vector3 size, Entity entity) {
    this.world          = world;
    positionComponent.getBulletMatrix(tempMat);
    this.ghostObject    = new btPairCachingGhostObject();
    ghostObject.setWorldTransform(tempMat);
    this.ghostShape     = new btCapsuleShape(size.x, size.y);
    ghostObject.setCollisionShape(ghostShape);
    ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);

    characterController = new btKinematicCharacterController(ghostObject, ghostShape, stepHeight);
    characterController.setJumpSpeed(jumpSpeed);
    characterController.setMaxJumpHeight(maxJumpHeight);
    characterController.setMaxSlope(maxSlope);
    world.addCollisionObject(
        ghostObject, (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
        (short)(btBroadphaseProxy.CollisionFilterGroups.StaticFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter)
    );
    world.addAction(characterController);
  }

  @Override
  public void reset() {
    super.reset();
    stepHeight = DEFAULT_STEP_HEIGHT;
    jumpSpeed  = DEFAULT_JUMP_SPEED;
    maxJumpHeight = DEFAULT_MAX_JUMP_HEIGHT;
    maxSlope  = DEFAULT_SLOPE;
    speed.setZero();
  }

  @Override
  public void disposeBullet() {
    world.removeAction(characterController);
    world.removeCollisionObject(ghostObject);
    characterController.dispose();
    ghostShape.dispose();
    ghostObject.dispose();
  }

  @Override
  public void set(BaseComponent otherComponent) {
    CharacterComponent chc = (CharacterComponent)otherComponent;
    this.speed.set( chc.speed );
    this.jumpSpeed = chc.jumpSpeed;
    maxJumpHeight = chc.maxJumpHeight;
    maxSlope      = chc.maxSlope;
  }
}
