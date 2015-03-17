package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import macbury.forge.components.PositionComponent;
import macbury.forge.level.Level;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.terrain.TerrainEngine;

/**
 * Created by macbury on 20.10.14.
 * Rebuild periodicaly whole tree for all entities passed into it
 */
public class OctreeSystem extends IntervalIteratingSystem {
  private static final float UPDATE_EVERY = 0.1f;
  private final OctreeNode tree;
  private final TerrainEngine terrainEngine;
  private final ChunkMap map;
  private ComponentMapper<PositionComponent> pm  = ComponentMapper.getFor(PositionComponent.class);

  public OctreeSystem(Level level) {
    super(Family.getFor(PositionComponent.class), UPDATE_EVERY);
    this.tree          = level.octree;
    this.terrainEngine = level.terrainEngine;
    this.map           = level.terrainMap;
  }

  @Override
  protected void updateInterval() {
    tree.clear();
    for (int i = 0; i < map.chunks.size; i++) {
      tree.insert(map.chunks.get(i));
    }
    super.updateInterval();
  }

  @Override
  protected void processEntity(Entity entity) {
    PositionComponent position = pm.get(entity);
    tree.insert(position);
  }
}
