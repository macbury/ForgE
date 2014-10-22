package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Array;
import macbury.forge.components.Position;
import macbury.forge.components.Visible;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 22.10.14.
 */
public class CullingSystem extends IteratingSystem {
  private final OctreeNode rootNode;
  private final PerspectiveCamera camera;
  private final Array<OctreeObject> octreeVisibleObjects;
  private ComponentMapper<Position>   pm = ComponentMapper.getFor(Position.class);
  private ComponentMapper<Visible>    vm = ComponentMapper.getFor(Visible.class);

  public CullingSystem(OctreeNode rootNode, PerspectiveCamera camera) {
    super(Family.getFor(Position.class, Visible.class));

    this.rootNode              = rootNode;
    this.camera                = camera;
    this.octreeVisibleObjects  = new Array<OctreeObject>();
  }

  @Override
  public void update(float deltaTime) {
    octreeVisibleObjects.clear();
    rootNode.retrieve(octreeVisibleObjects, camera.frustum);
    super.update(deltaTime);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Visible visible   = vm.get(entity);
    Position position = pm.get(entity);

    visible.visible   = octreeVisibleObjects.contains(position, false);
  }
}
