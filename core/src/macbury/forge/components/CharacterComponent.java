package macbury.forge.components;

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
  private final static float DEFAULT_STEP_HEIGHT = 0.35f;
  public btPairCachingGhostObject ghostObject;
  private btCapsuleShape ghostShape;
  public btKinematicCharacterController characterController;
  private float stepHeight = DEFAULT_STEP_HEIGHT;

  @Override
  public void initBullet(Matrix4 transform, btDiscreteDynamicsWorld world, Vector3 size) {
    this.world          = world;
    this.ghostObject    = new btPairCachingGhostObject();
    ghostObject.setWorldTransform(transform);
    this.ghostShape     = new btCapsuleShape(size.x, size.y);
    ghostObject.setCollisionShape(ghostShape);
    ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);

    characterController = new btKinematicCharacterController(ghostObject, ghostShape, stepHeight);

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

  }
}
