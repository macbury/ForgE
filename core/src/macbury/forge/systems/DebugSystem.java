package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.ForgE;
import macbury.forge.components.Position;
import macbury.forge.graphics.DebugShape;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.Level;
import macbury.forge.octree.OctreeNode;

/**
 * Created by macbury on 20.10.14.
 */
public class DebugSystem extends IteratingSystem {
  private static final Color BOUNDING_BOX_COLOR  = Color.DARK_GRAY;
  private static final Color OCTREE_BOUNDS_COLOR = Color.OLIVE;
  private final VoxelBatch batch;
  private final PerspectiveCamera camera;
  private final OctreeNode octree;
  private final FrustrumDebugAndRenderer frustrumDebugger;
  private ComponentMapper<Position>   pm = ComponentMapper.getFor(Position.class);
  private final BoundingBox tempBox;
  private final Vector3     tempVec;

  public DebugSystem(Level level) {
    super(Family.getFor(Position.class));
    this.batch   = level.batch;
    this.octree  = level.octree;
    this.camera  = level.camera;
    this.frustrumDebugger = level.frustrumDebugger;
    this.tempBox = new BoundingBox();
    this.tempVec = new Vector3();
  }

  @Override
  public boolean checkProcessing() {
    return false;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    Position positionComponent = pm.get(entity);

    tempBox.set(positionComponent.getBoundingBox());

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
      }

      batch.shapeRenderer.setColor(OCTREE_BOUNDS_COLOR);
      if (ForgE.config.renderOctree) {
        Frustum currentFrustrum = camera.frustum;
        if (frustrumDebugger.isEnabled()) {
          currentFrustrum = frustrumDebugger.getFrustrum();
        }
        DebugShape.cullledOctree(batch.shapeRenderer, octree, currentFrustrum);
      }
    } batch.shapeRenderer.end();

    frustrumDebugger.render(camera);
  }
}
