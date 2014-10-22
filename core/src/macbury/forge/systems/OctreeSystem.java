package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import macbury.forge.components.Position;
import macbury.forge.octree.OctreeNode;

/**
 * Created by macbury on 20.10.14.
 * Rebuild periodicaly whole tree for all entities passed into it
 */
public class OctreeSystem extends IntervalIteratingSystem {
  private static final float UPDATE_EVERY = 0.1f;
  private final OctreeNode tree;
  private ComponentMapper<Position> pm  = ComponentMapper.getFor(Position.class);

  public OctreeSystem(OctreeNode tree) {
    super(Family.getFor(Position.class), UPDATE_EVERY);
    this.tree = tree;
  }


  @Override
  protected void updateInterval() {
    tree.clear();
    super.updateInterval();
  }

  @Override
  protected void processEntity(Entity entity) {
    Position position = pm.get(entity);
    tree.insert(position);
  }
}
