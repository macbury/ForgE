package macbury.forge.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.components.*;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.ChunkPartCollider;
import macbury.forge.terrain.TerrainEngine;

/**
 * Created by macbury on 09.04.15.
 */
public class PhysicsSystem extends EntitySystem implements EntityListener, Disposable, TerrainEngine.TerrainEngineListener {
  private static final String TAG            = "PhysicsSystem";
  private static final float MIN_TIME_STEPS  = 1f / 30f;
  private static final int MAX_SUB_STEPS     = 5;
  private static final float FIXED_TIME_STEP = 1f / 60f;
  public static final float BULLET_SIZE = 0.5f;

  public enum Flags {
    All(-1), Object(1<<9), Ground(1<<8);
    public final short mask;
    Flags(int mask) {
      this.mask = (short)mask;
    }
  }


  private final Family familyCharacterAndPosition;
  private DebugDrawer debugDrawer;
  private btSequentialImpulseConstraintSolver constraintSolver;
  private btAxisSweep3 sweep;
  private ComponentMapper<PositionComponent> pm   = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<CharacterComponent> chm   = ComponentMapper.getFor(CharacterComponent.class);

  private btDefaultCollisionConfiguration collisionConfig;
  private btCollisionDispatcher dispatcher;
  private btDiscreteDynamicsWorld bulletWorld;
  private final Vector3 tempA = new Vector3();

  private btGhostPairCallback ghostCallback;
  private Array<Entity> entitiesWithBullet = new Array<Entity>();

  public PhysicsSystem() {
    super();
    familyCharacterAndPosition = Family.getFor(PositionComponent.class);
    createWorld();
  }

  private void createWorld() {
    debugDrawer      = new DebugDrawer();
    collisionConfig  = new btDefaultCollisionConfiguration();
    dispatcher       = new btCollisionDispatcher(collisionConfig);
    constraintSolver = new btSequentialImpulseConstraintSolver();
    sweep            = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
    ghostCallback    = new btGhostPairCallback();
    sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostCallback);
    bulletWorld      = new btDiscreteDynamicsWorld(dispatcher, sweep, constraintSolver, collisionConfig);

    bulletWorld.setGravity(new Vector3(0, -9f, 0));
    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
  }

  @Override
  public void update(float deltaTime) {
    bulletWorld.stepSimulation(Math.min(MIN_TIME_STEPS, deltaTime), MAX_SUB_STEPS, FIXED_TIME_STEP);
  }

  private boolean isInFamily(Entity e) {
    return familyCharacterAndPosition.matches(e);
  }

  @Override
  public void entityAdded(Entity entity) {
    if (isInFamily(entity)) {
      entitiesWithBullet.add(entity);
      initEntityBulletStuff(entity);
    }
  }

  @Override
  public void entityRemoved(Entity entity) {
    if (isInFamily(entity)) {
      entitiesWithBullet.removeValue(entity, true);
      disposeEntityBulletStuff(entity);
    }
  }

  @Override
  public void onChunkRemove(Chunk chunk, TerrainEngine engine) {
    for (ChunkPartCollider collider : chunk.colliders) {
      collider.dispose();
    }
  }

  @Override
  public void onChunkAdded(Chunk chunk, TerrainEngine engine) {
    for (ChunkPartCollider collider : chunk.colliders) {
      collider.initializeAndAddToWorld(bulletWorld);
    }

  }

  private void initEntityBulletStuff(Entity entity) {
    ForgE.log(TAG, "Added entity");
    PositionComponent positionComponent = pm.get(entity);
    positionComponent.updateTransformMatrix();
    for (int i = 0; i < entity.getComponents().size(); i++) {
      Component component = entity.getComponents().get(i);

      if (BulletPsychicsComponent.class.isInstance(component)) {
        BulletPsychicsComponent bulletPsychicsComponent = (BulletPsychicsComponent)component;
        tempA.set(positionComponent.size).scl(PhysicsSystem.BULLET_SIZE);
        bulletPsychicsComponent.initBullet(positionComponent, bulletWorld,tempA, entity);
      }
    }
  }

  private void disposeEntityBulletStuff(Entity entity) {
    ForgE.log(TAG, "Removing entity");
    for (int i = 0; i < entity.getComponents().size(); i++) {
      Component component = entity.getComponents().get(i);

      if (BulletPsychicsComponent.class.isInstance(component)) {
        BulletPsychicsComponent bulletPsychicsComponent = (BulletPsychicsComponent)component;
        bulletPsychicsComponent.disposeBullet();
      }
    }
  }

  public void debugDraw(PerspectiveCamera camera) {
    bulletWorld.setDebugDrawer(debugDrawer);
    debugDrawer.begin(camera); {
      bulletWorld.debugDrawWorld();
    } debugDrawer.end();
  }

  @Override
  public void dispose() {
    for (Entity e : entitiesWithBullet) {
      disposeEntityBulletStuff(e);
    }
    ghostCallback.dispose();
    dispatcher.dispose();
    collisionConfig.dispose();

    bulletWorld.dispose();
    debugDrawer.dispose();
    constraintSolver.dispose();
    sweep.dispose();
    entitiesWithBullet.clear();
  }

  public void disable() {
    setProcessing(false);
  }

  public class VoxelChunkRigidBody {
    @Override
    public boolean equals(Object obj) {
      return super.equals(obj);
    }
  }
}
