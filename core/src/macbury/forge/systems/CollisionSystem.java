package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.components.CollisionComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.octree.OctreeNode;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;

import javax.swing.text.Position;

/**
 * Created by macbury on 21.03.15.
 */
public class CollisionSystem extends IteratingSystem {
  private static final String TAG = "CollisionSystem";
  private ComponentMapper<PositionComponent> pm   = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm   = ComponentMapper.getFor(MovementComponent.class);
  private ComponentMapper<CollisionComponent> cm  = ComponentMapper.getFor(CollisionComponent.class);
  private final OctreeNode octree;
  private final ChunkMap map;
  private final Vector3 tempA;
  private final Vector3 tempB;
  private final Vector3i tempC;

  public CollisionSystem(ChunkMap terrainMap, OctreeNode octree) {
    super(Family.getFor(MovementComponent.class, PositionComponent.class, CollisionComponent.class));
    this.octree = octree;
    this.map    = terrainMap;
    tempA = new Vector3();
    tempB = new Vector3();
    tempC = new Vector3i();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent   = pm.get(entity);
    CollisionComponent collisionComponent = cm.get(entity);
    MovementComponent movementComponent   = mm.get(entity);
    collisionComponent.position.setZero();

    if (collisionComponent.solid) {
      tempC.set(map.worldPositionToVoxelPosition(positionComponent.vector));
      if (map.isSolid(tempC.x, tempC.y, tempC.z) || map.isOutOfBounds(tempC)) {
        //Gdx.app.log(TAG, "Collision ground: "+tempC.toString());
        //map.localVoxelPositionToWorldPosition(tempC, tempA);

        collisionComponent.position.y    = tempC.y + 0.5f;
      }
    }
    //TODO: check collision with terrain
    //TODO: check collision with entities

  }
}
