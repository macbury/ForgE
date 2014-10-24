package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.ForgE;
import macbury.forge.components.Position;
import macbury.forge.graphics.DebugShape;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.Level;
import macbury.forge.octree.OctreeNode;
import macbury.forge.terrain.TerrainEngine;

/**
 * Created by macbury on 20.10.14.
 */
public class DebugSystem extends IteratingSystem {
  private static final Color BOUNDING_BOX_COLOR  = Color.DARK_GRAY;
  private static final Color OCTREE_BOUNDS_COLOR = Color.OLIVE;
  private final VoxelBatch batch;
  private final GameCamera camera;
  private final OctreeNode dynamicOctree;
  private final FrustrumDebugAndRenderer frustrumDebugger;
  private final OctreeNode terrainOctree;
  private final TerrainEngine terrain;
  private ComponentMapper<Position>   pm = ComponentMapper.getFor(Position.class);
  private final BoundingBox tempBox;
  private final Vector3     tempVec;

  public DebugSystem(Level level) {
    super(Family.getFor(Position.class));
    this.batch            = level.batch;
    this.dynamicOctree    = level.dynamicOctree;
    this.terrainOctree    = level.staticOctree;
    this.camera           = level.camera;
    this.terrain          = level.terrainEngine;
    this.frustrumDebugger = level.frustrumDebugger;
    this.tempBox          = new BoundingBox();
    this.tempVec          = new Vector3();
  }

  @Override
  public boolean checkProcessing() {
    return false;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    Position positionComponent = pm.get(entity);

    positionComponent.getBoundingBox(tempBox);
    if (positionComponent.visible) {
      DebugShape.draw(batch.shapeRenderer, tempBox);
    }
  }

  @Override
  public void update(float deltaTime) {

    batch.shapeRenderer.setProjectionMatrix(camera.combined);

    batch.shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
      batch.shapeRenderer.setColor(BOUNDING_BOX_COLOR);
      if (ForgE.config.renderBoundingBox) {
        super.update(deltaTime);

        for (int i = 0; i < terrain.chunks.size; i++) {
          batch.shapeRenderer.setColor(BOUNDING_BOX_COLOR);
          Chunk chunk = terrain.chunks.get(i);
          chunk.getBoundingBox(tempBox);

          DebugShape.draw(batch.shapeRenderer, tempBox);
        }
      }

      batch.shapeRenderer.setColor(OCTREE_BOUNDS_COLOR);
      if (ForgE.config.renderDynamicOctree) {
        DebugShape.cullledOctree(batch.shapeRenderer, dynamicOctree, camera.normalOrDebugFrustrum());
      }

      if (ForgE.config.renderStaticOctree) {
        DebugShape.cullledOctree(batch.shapeRenderer, terrainOctree, camera.normalOrDebugFrustrum());
      }
    } batch.shapeRenderer.end();

    frustrumDebugger.render(camera);
  }
}
