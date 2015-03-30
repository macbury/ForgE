package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.components.CursorComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.graphics.DebugShape;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.batch.sprites.Sprite3D;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.Level;
import macbury.forge.octree.OctreeNode;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 20.10.14.
 */
public class DebugSystem extends IteratingSystem implements Disposable {
  private static final Color BOUNDING_BOX_COLOR  = Color.DARK_GRAY;
  private static final Color OCTREE_BOUNDS_COLOR = Color.OLIVE;
  private static final float DEBUG_BOX_OFFSET_POSITION    = 0.05f;
  private static final float DEBUG_BOX_OFFSET_SIZE        = DEBUG_BOX_OFFSET_POSITION * 2;
  private final VoxelBatch batch;
  private final GameCamera camera;
  private final OctreeNode dynamicOctree;
  private final FrustrumDebugAndRenderer frustrumDebugger;
  private final OctreeNode terrainOctree;
  private final TerrainEngine terrain;
  private final RenderContext context;
  private final Level level;
  private Texture startPositionIcon;
  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<CursorComponent>     cm = ComponentMapper.getFor(CursorComponent.class);
  private final BoundingBox tempBox;
  private final Vector3     tempVec;
  private Sprite3D          startPositionSprite;

  public DebugSystem(Level level) {
    super(Family.getFor(PositionComponent.class));
    this.level            = level;
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
      if (ForgE.config.debug) {
        renderStartPosition();
      }

    } batch.shapeRenderer.end();

    frustrumDebugger.render(camera);
    context.end();
  }

  private void renderStartPosition() {
    if (ForgE.db.startPosition != null && level.state.id == ForgE.db.startPosition.mapId) {

      level.terrainMap.localVoxelPositionToWorldPosition(ForgE.db.startPosition.voxelPosition, tempVec);
      renderDebugBox(tempVec, level.terrainMap.voxelSize, Color.WHITE, Color.BLACK);
      if (startPositionIcon == null) {
        this.startPositionSprite = batch.build(ForgE.assets.getTexture("ed/icons/start_position.png"), false, true);
      }
      startPositionSprite.set(tempVec.sub(-0.5f));

      startPositionSprite.lookAt(camera.position, camera.up);
      batch.add(startPositionSprite);
    }
  }

  private void renderDebugBox(Vector3 position, Vector3 size, Color outerColor, Color innerColor) {
    batch.shapeRenderer.setColor(outerColor);
    batch.shapeRenderer.box(position.x, position.y, position.z+size.z, size.x,size.y,size.z);
    batch.shapeRenderer.setColor(innerColor);
    batch.shapeRenderer.box(
        position.x + DEBUG_BOX_OFFSET_POSITION,
        position.y + DEBUG_BOX_OFFSET_POSITION,
        position.z+size.z - DEBUG_BOX_OFFSET_POSITION,
        size.x - DEBUG_BOX_OFFSET_SIZE,
        size.y - DEBUG_BOX_OFFSET_SIZE,
        size.z - DEBUG_BOX_OFFSET_SIZE
    );
  }


  @Override
  public void dispose() {
    if (startPositionIcon != null)
      startPositionIcon.dispose();
  }
}
