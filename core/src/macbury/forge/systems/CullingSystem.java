package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import macbury.forge.components.PositionComponent;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.Level;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.octree.query.FrustrumClassFilterOctreeQuery;

/**
 * Created by macbury on 22.10.14.
 */
public class CullingSystem extends IteratingSystem {
  private final OctreeNode rootNode;
  private final GameCamera camera;
  private final Array<OctreeObject> octreeVisibleObjects;
  private final FrustrumDebugAndRenderer frustrumDebugger;
  private final FrustrumClassFilterOctreeQuery frustrumOctreeQuery;
  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private float cameraOldFieldOfView;

  public CullingSystem(Level level) {
    super(Family.getFor(PositionComponent.class));

    this.rootNode              = level.octree;
    this.camera                = level.camera;
    this.frustrumDebugger      = level.frustrumDebugger;
    this.octreeVisibleObjects  = new Array<OctreeObject>();
    this.frustrumOctreeQuery   = new FrustrumClassFilterOctreeQuery();
    frustrumOctreeQuery.setKlass(PositionComponent.class);
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    octreeVisibleObjects.clear();
    camera.extendFov(); {
      frustrumOctreeQuery.setFrustum(camera.normalOrDebugFrustrum());
      rootNode.retrieve(octreeVisibleObjects, frustrumOctreeQuery);

      for (int i = 0; i < octreeVisibleObjects.size; i++) {
        PositionComponent position = (PositionComponent) octreeVisibleObjects.get(i);
        //position.visible  = true;
      }

    } camera.restoreFov();

    //
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent position = pm.get(entity);
    //position.visible  = false;
  }
}
