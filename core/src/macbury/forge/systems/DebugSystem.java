package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.ForgE;
import macbury.forge.components.CursorComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.graphics.DebugShape;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
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
  private final RenderContext context;
  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<CursorComponent>     cm = ComponentMapper.getFor(CursorComponent.class);
  private final BoundingBox tempBox;
  private final Vector3     tempVec;

  public DebugSystem(Level level) {
    super(Family.getFor(PositionComponent.class));
    this.batch            = level.batch;
    this.context          = level.renderContext;
    this.dynamicOctree    = level.octree;
    this.terrainOctree    = level.octree;
    this.camera           = level.camera;
    this.terrain          = level.terrainEngine;
    this.frustrumDebugger = level.frustrumDebugger;
    this.tempBox          = new BoundingBox();
    this.tempVec          = new Vector3();
  }


  @Override
  public void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent = pm.get(entity);
    CursorComponent cursorComponent   = cm.get(entity);

    if (positionComponent.visible && ForgE.config.renderBoundingBox) {
      positionComponent.getBoundingBox(tempBox);
      DebugShape.draw(batch.shapeRenderer, tempBox);
    }

    if (cursorComponent != null) {
      batch.shapeRenderer.setColor(cursorComponent.color);
      DebugShape.draw(batch.shapeRenderer, cursorComponent.cursorBox);
    }
  }

  @Override
  public void update(float deltaTime) {
    context.begin();
    context.setDepthTest(GL30.GL_LEQUAL);
    context.setCullFace(GL30.GL_BACK);
    context.setDepthTest(GL20.GL_LEQUAL);
    batch.shapeRenderer.setProjectionMatrix(camera.combined);

    batch.shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
      batch.shapeRenderer.setColor(BOUNDING_BOX_COLOR);
      batch.shapeRenderer.identity();
      super.update(deltaTime);
      if (ForgE.config.renderBoundingBox) {
        batch.shapeRenderer.identity();
        for (int i = 0; i < terrain.chunks.size; i++) {
          //batch.shapeRenderer.setColor(BOUNDING_BOX_COLOR);
          Chunk chunk = terrain.chunks.get(i);
          chunk.getBoundingBox(tempBox);

          //DebugShape.draw(batch.shapeRenderer, tempBox);
          //batch.shapeRenderer.setColor(Color.NAVY);
          for (int j = 0; j < chunk.renderables.size; j++) {
            VoxelFaceRenderable renderable = chunk.renderables.get(j);
            DebugShape.draw(batch.shapeRenderer, renderable.boundingBox);
          }
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
    context.end();
  }
}
