package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import macbury.forge.components.BoundBox;
import macbury.forge.components.Position;
import macbury.forge.components.Visible;
import macbury.forge.octree.OctreeNode;

/**
 * Created by macbury on 20.10.14.
 * Rebuild periodicaly whole tree for all entities passed into it
 */
public class OctreeSystem extends IntervalIteratingSystem implements EntityListener {
  private static final float UPDATE_EVERY = 0.1f;
  private final OctreeNode tree;

  public OctreeSystem(OctreeNode tree) {
    super(Family.getFor(ComponentType.getBitsFor(Visible.class), ComponentType.getBitsFor(Position.class, BoundBox.class), ComponentType.getBitsFor()), UPDATE_EVERY);
    this.tree = tree;
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {

  }

  @Override
  protected void updateInterval() {
    tree.clear();
    super.updateInterval();
  }

  @Override
  protected void processEntity(Entity entity) {

  }
}
