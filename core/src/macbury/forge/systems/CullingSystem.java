package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import macbury.forge.components.Position;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.Level;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 22.10.14.
 */
public class CullingSystem extends IteratingSystem {
  private final OctreeNode rootNode;
  private final GameCamera camera;
  private final Array<OctreeObject> octreeVisibleObjects;
  private final FrustrumDebugAndRenderer frustrumDebugger;
  private ComponentMapper<Position>   pm = ComponentMapper.getFor(Position.class);
  private float cameraOldFieldOfView;

  public CullingSystem(Level level) {
    super(Family.getFor(Position.class));

    this.rootNode              = level.dynamicOctree;
    this.camera                = level.camera;
    this.frustrumDebugger      = level.frustrumDebugger;
    this.octreeVisibleObjects  = new Array<OctreeObject>();
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    octreeVisibleObjects.clear();

    camera.extendFov();
    rootNode.retrieve(octreeVisibleObjects, camera.normalOrDebugFrustrum(), true);

    camera.fieldOfView   = cameraOldFieldOfView;
    camera.update();

    for (int i = 0; i < octreeVisibleObjects.size; i++) {
      Position position = (Position) octreeVisibleObjects.get(i);
      position.visible  = true;
    }

    camera.restoreFov();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = pm.get(entity);
    position.visible  = false;
  }
}
