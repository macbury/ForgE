package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.utils.Array;
import macbury.forge.components.Position;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.Level;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 22.10.14.
 */
public class CullingSystem extends IteratingSystem {
  private final OctreeNode rootNode;
  private final PerspectiveCamera camera;
  private final Array<OctreeObject> octreeVisibleObjects;
  private final FrustrumDebugAndRenderer frustrumDebugger;
  private ComponentMapper<Position>   pm = ComponentMapper.getFor(Position.class);
  private float cameraOldFieldOfView;

  public CullingSystem(Level level) {
    super(Family.getFor(Position.class));

    this.rootNode              = level.octree;
    this.camera                = level.camera;
    this.frustrumDebugger      = level.frustrumDebugger;
    this.octreeVisibleObjects  = new Array<OctreeObject>();
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    octreeVisibleObjects.clear();
    Frustum currentFrustrum;

    if (frustrumDebugger.isEnabled()) {
      currentFrustrum = frustrumDebugger.getFrustrum();
    } else {
      cameraOldFieldOfView = camera.fieldOfView;
      camera.fieldOfView   += 10;
      camera.update();
      currentFrustrum = camera.frustum;
    }

    rootNode.retrieve(octreeVisibleObjects, currentFrustrum, true);

    camera.fieldOfView   = cameraOldFieldOfView;
    camera.update();

    for (int i = 0; i < octreeVisibleObjects.size; i++) {
      Position position = (Position) octreeVisibleObjects.get(i);
      position.visible  = true;
    }
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = pm.get(entity);
    position.visible  = false;
  }
}
