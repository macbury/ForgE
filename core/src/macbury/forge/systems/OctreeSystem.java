package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import macbury.forge.components.BoundBox;
import macbury.forge.components.Position;
import macbury.forge.components.Visible;
import macbury.forge.octree.OctreeNode;

/**
 * Created by macbury on 20.10.14.
 */
public class OctreeSystem extends IteratingSystem implements EntityListener {
  private final OctreeNode tree;

  public OctreeSystem(OctreeNode tree) {
    super(Family.getFor(ComponentType.getBitsFor(Visible.class), ComponentType.getBitsFor(Position.class, BoundBox.class), ComponentType.getBitsFor()));
    this.tree = tree;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {

  }
}
