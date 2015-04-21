package macbury.forge.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.components.CollisionComponent;
import macbury.forge.components.GravityComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.utils.MyMath;

/**
 * Created by macbury on 09.04.15.
 */
public class PsychicsSystem extends EntitySystem implements EntityListener, Disposable {
  private final DebugDrawer debugDrawer;
  private final btCollisionObject groundCollider2;
  private final btBoxShape groundShape2;
  private ComponentMapper<PositionComponent> pm   = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<CollisionComponent> cm  = ComponentMapper.getFor(CollisionComponent.class);
  private ComponentMapper<MovementComponent> mm   = ComponentMapper.getFor(MovementComponent.class);

  private final btDefaultCollisionConfiguration collisionConfig;
  private final btCollisionDispatcher dispatcher;
  private final btDbvtBroadphase broadphase;
  private final btCollisionWorld collisionWorld;
  private final btBoxShape groundShape;
  private final btCollisionObject groundCollider;
  private final PsychicsContactListener contactListener;
  private final Family family;

  private final btCapsuleShape playerCapsuleShape;
  private final btCollisionObject playerCollisionObject;

  private ImmutableArray<Entity> entities;

  public PsychicsSystem() {
    super();

    family          = Family.getFor(PositionComponent.class, CollisionComponent.class);

    debugDrawer     = new DebugDrawer();
    collisionConfig = new btDefaultCollisionConfiguration();
    dispatcher      = new btCollisionDispatcher(collisionConfig);
    broadphase      = new btDbvtBroadphase();
    contactListener = new PsychicsContactListener();
    collisionWorld  = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

    Matrix4 groundPos = new Matrix4();
    groundPos.translate(0,0,0);

    groundShape     = new btBoxShape(new Vector3(100f, 1f, 100f));
    groundCollider  = new btCollisionObject();
    groundCollider.setCollisionShape(groundShape);
    groundCollider.setWorldTransform(groundPos);
    groundCollider.setCollisionFlags(btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);

    Matrix4 groundPos2 = new Matrix4();
    groundPos2.translate(25,2,25);

    groundShape2     = new btBoxShape(new Vector3(20f, 2f, 20f));
    groundCollider2  = new btCollisionObject();
    groundCollider2.setCollisionShape(groundShape2);
    groundCollider2.setWorldTransform(groundPos2);
    groundCollider2.setCollisionFlags(btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);

    playerCapsuleShape    = new btCapsuleShape(0.5f, 1f);
    playerCollisionObject = new btCollisionObject();
    playerCollisionObject.setCollisionShape(playerCapsuleShape);

    playerCollisionObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

    collisionWorld.addCollisionObject(groundCollider2, Flags.GROUND, Flags.ALL);
    collisionWorld.addCollisionObject(groundCollider, Flags.GROUND, Flags.ALL);
    collisionWorld.addCollisionObject(playerCollisionObject, Flags.GROUND, Flags.GROUND);
  }

  @Override
  public void update(float deltaTime) {
    for (int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);
      PositionComponent pc   = pm.get(e);

      playerCollisionObject.setWorldTransform(pc.worldTransform);
    }

    collisionWorld.performDiscreteCollisionDetection();
  }

  @Override
  public void removedFromEngine(Engine engine) {
    entities = null;
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(family);
  }

  @Override
  public void entityAdded(Entity entity) {
    if (family.matches(entity)) {
      CollisionComponent cc = cm.get(entity);
      playerCollisionObject.userData = entity;
      ////entityArray.add(entity);
    }
  }

  @Override
  public void entityRemoved(Entity entity) {
    if (family.matches(entity)) {
      //entityArray.removeValue(entity, true);
      //TODO clear bullet shit
    }
  }

  public void debugDraw(PerspectiveCamera camera) {
    collisionWorld.setDebugDrawer(debugDrawer);
    debugDrawer.begin(camera); {
      collisionWorld.debugDrawWorld();
    }debugDrawer.end();
  }

  @Override
  public void dispose() {
    groundCollider2.dispose();
    groundShape2.dispose();

    playerCollisionObject.dispose();
    playerCapsuleShape.dispose();

    groundShape.dispose();
    groundCollider.dispose();

    entities = null;

    dispatcher.dispose();
    collisionConfig.dispose();

    contactListener.dispose();
    collisionWorld.dispose();

    broadphase.dispose();
    debugDrawer.dispose();
  }

  public void disable() {
    setProcessing(false);
  }

  public static class Flags {
    final static short GROUND = 1<<8;
    final static short OBJECT = 1<<9;
    final static short ALL    = -1;
  }

  public class PsychicsContactListener extends ContactListener {
    private static final String TAG = "PsychicsContactListener";
    private Vector3 norm = new Vector3();
    private Vector3 worldA = new Vector3();
    private Vector3 worldB = new Vector3();

/*
    @Override
    public void onContactStarted(btPersistentManifold manifold) {
      if (manifold.getNumContacts() > 0) {

        Gdx.app.log(TAG, "Contact started");
        btCollisionObject colObjA = manifold.getBody0();
        btCollisionObject colObjB = manifold.getBody1();

        Entity eA = getEntity(colObjA);
        Entity eB = getEntity(colObjB);

        if (eA != null || eB != null) {
          for (int i = 0; i < manifold.getNumContacts(); i++) {
            btManifoldPoint cp = manifold.getContactPoint(i);
            float dist = cp.getDistance();
            cp.getNormalWorldOnB(norm);
            cp.getPositionWorldOnA(worldA);
            cp.getPositionWorldOnA(worldB);

          /*Gdx.app.log(TAG, "Distance: " + dist);
          Gdx.app.log(TAG, "worldA: " + worldA.toString());
          Gdx.app.log(TAG, "worldB: " + worldB.toString());
          Gdx.app.log(TAG, "norm: " + norm.nor().toString());

            if (eA != null) {
              startEntityCollision(eA, norm, worldA, worldB, dist, eB);
            }

            if (eB != null) {
              startEntityCollision(eB, norm.scl(-1f), worldB, worldA, dist, eA);
            }

            cp.dispose();
          }
        }
      }
    }
*/

    @Override
    public boolean onContactAdded(btManifoldPoint cp, btCollisionObject colObjA, int partId0, int index0, btCollisionObject colObjB, int partId1, int index1) {
      Entity eA = getEntity(colObjA);
      Entity eB = getEntity(colObjB);

      if (eA != null || eB != null) {
        Gdx.app.log(TAG, "Contact Added");
        float dist = cp.getDistance();
        cp.getNormalWorldOnB(norm);
        cp.getPositionWorldOnA(worldA);
        cp.getPositionWorldOnA(worldB);

        if (eA != null) {
          startEntityCollision(eA, norm, worldA, worldB, dist, eB);
        }

        if (eB != null) {
          startEntityCollision(eB, norm.scl(-1f), worldB, worldA, dist, eA);
        }
      }

      return true;
    }

    @Override
    public void onContactEnded(btPersistentManifold manifold) {
      btCollisionObject colObjA = manifold.getBody0();
      btCollisionObject colObjB = manifold.getBody1();

      Entity eA = getEntity(colObjA);
      Entity eB = getEntity(colObjB);
      if (eA != null || eB != null) {
        if (eA != null) {
          stopEntityCollision(eA);
        }

        if (eB != null) {
          stopEntityCollision(eB);
        }
        Gdx.app.log(TAG, "Contact ended");
      }
    }

    private void stopEntityCollision(Entity ent) {
      CollisionComponent collisionComponent = cm.get(ent);
      if (collisionComponent != null) {
        collisionComponent.normal.setZero();
      }
    }

    private void startEntityCollision(Entity ent, Vector3 normal, Vector3 myPoint, Vector3 otherPoint, float dist, Entity otherEntity) {
      CollisionComponent collisionComponent = cm.get(ent);
      if (collisionComponent != null) {
        collisionComponent.normal.add(normal);
      }
    }

    private Entity getEntity(btCollisionObject collisionObject) {
      if (collisionObject.userData != null) {
        return (Entity)collisionObject.userData;
      } else {
        return null;
      }
    }

  }
}
