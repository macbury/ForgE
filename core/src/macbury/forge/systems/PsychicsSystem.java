package macbury.forge.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 09.04.15.
 */
public class PsychicsSystem extends EntitySystem implements EntityListener, Disposable {

  private final btDefaultCollisionConfiguration collisionConfig;
  private final btCollisionDispatcher dispatcher;
  private final btDbvtBroadphase broadphase;
  private final btCollisionWorld collisionWorld;
  private final btBoxShape groundShape;
  private final btCollisionObject groundCollider;
  private final PsychicsContactListener contactListener;

  public PsychicsSystem() {
    super();
    collisionConfig = new btDefaultCollisionConfiguration();
    dispatcher      = new btCollisionDispatcher(collisionConfig);
    broadphase      = new btDbvtBroadphase();
    collisionWorld  = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
    contactListener = new PsychicsContactListener();

    groundShape     = new btBoxShape(new Vector3(100f, 0.5f, 100f));
    groundCollider  = new btCollisionObject();
    groundCollider.setCollisionShape(groundShape);

    collisionWorld.addCollisionObject(groundCollider);

  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    collisionWorld.performDiscreteCollisionDetection();
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {

  }

  @Override
  public void dispose() {
    groundShape.dispose();
    groundCollider.dispose();

    dispatcher.dispose();
    collisionConfig.dispose();

    contactListener.dispose();
    collisionWorld.dispose();

    broadphase.dispose();
  }

  public class PsychicsContactListener extends ContactListener {
    @Override
    public boolean onContactAdded(btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1, int partId1, int index1) {
      return super.onContactAdded(colObj0, partId0, index0, colObj1, partId1, index1);
    }
  }
}
