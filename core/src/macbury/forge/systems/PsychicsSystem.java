package macbury.forge.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.components.CollisionComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 09.04.15.
 */
public class PsychicsSystem extends EntitySystem implements EntityListener, Disposable {
  private static final String TAG            = "PsychicsSystem";
  private static final float MIN_TIME_STEPS  = 1f / 30f;
  private static final int MAX_SUB_STEPS     = 5;
  private static final float FIXED_TIME_STEP = 1f / 60f;
  private DebugDrawer debugDrawer;
  private btSequentialImpulseConstraintSolver constraintSolver;
  private btAxisSweep3 sweep;
  private ComponentMapper<PositionComponent> pm   = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<CollisionComponent> cm  = ComponentMapper.getFor(CollisionComponent.class);
  private ComponentMapper<MovementComponent> mm   = ComponentMapper.getFor(MovementComponent.class);

  private btDefaultCollisionConfiguration collisionConfig;
  private btCollisionDispatcher dispatcher;
  private btDiscreteDynamicsWorld bulletWorld;
  private final Family family;

  public PsychicsSystem() {
    super();
    family          = Family.getFor(PositionComponent.class, CollisionComponent.class);
    createWorld();
  }

  private void createWorld() {
    debugDrawer      = new DebugDrawer();
    collisionConfig  = new btDefaultCollisionConfiguration();
    dispatcher       = new btCollisionDispatcher(collisionConfig);
    constraintSolver = new btSequentialImpulseConstraintSolver();
    sweep            = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
    bulletWorld      = new btDiscreteDynamicsWorld(dispatcher, sweep, constraintSolver, collisionConfig);

    bulletWorld.setGravity(new Vector3(0, -10f, 0));
    debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
  }

  @Override
  public void update(float deltaTime) {
    bulletWorld.stepSimulation(Math.min(MIN_TIME_STEPS, deltaTime), MAX_SUB_STEPS, FIXED_TIME_STEP);
  }

  @Override
  public void entityAdded(Entity entity) {
    if (family.matches(entity)) {
      Gdx.app.log(TAG, "Added entity");
      CollisionComponent cc = cm.get(entity);
      ////entityArray.add(entity);
    }
  }

  @Override
  public void entityRemoved(Entity entity) {
    if (family.matches(entity)) {
      Gdx.app.log(TAG, "Removed entity");
      //entityArray.removeValue(entity, true);
      //TODO clear bullet shit
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
    dispatcher.dispose();
    collisionConfig.dispose();

    bulletWorld.dispose();

    debugDrawer.dispose();
    constraintSolver.dispose();
    sweep.dispose();
  }

  public void disable() {
    setProcessing(false);
  }

  public static class Flags {
    final static short GROUND = 1<<8;
    final static short OBJECT = 1<<9;
    final static short ALL    = -1;
  }

}
