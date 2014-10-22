package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.components.Position;
import macbury.forge.components.Visible;
import macbury.forge.graphics.DebugShape;
import macbury.forge.graphics.batch.VoxelBatch;
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
  private ComponentMapper<Position> pm   = ComponentMapper.getFor(Position.class);
  private ComponentMapper<Visible>    vm = ComponentMapper.getFor(Visible.class);
  private final BoundingBox tempBox;
  private final Vector3     tempVec;

  public DebugSystem(Level level) {
    super(Family.getFor(ComponentType.getBitsFor(Position.class), ComponentType.getBitsFor(Visible.class), ComponentType.getBitsFor()));
    this.batch   = level.batch;
    this.octree  = level.octree;
    this.camera  = level.camera;
    this.tempBox = new BoundingBox();
    this.tempVec = new Vector3();
  }

  @Override
  public boolean checkProcessing() {
    return false;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    boolean render             = true;
    Visible  visibleComponent  = vm.get(entity);
    Position positionComponent = pm.get(entity);

    tempBox.set(positionComponent.getBoundingBox());

    if (visibleComponent != null)
      render = visibleComponent.visible;

    if (render) {
      DebugShape.draw(batch.shapeRenderer, tempBox);
    }
  }

  @Override
  public void update(float deltaTime) {
    batch.shapeRenderer.setProjectionMatrix(camera.combined);

    batch.shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
      batch.shapeRenderer.setColor(BOUNDING_BOX_COLOR);
      super.update(deltaTime);

      batch.shapeRenderer.setColor(OCTREE_BOUNDS_COLOR);
      DebugShape.octree(batch.shapeRenderer, octree);
    } batch.shapeRenderer.end();
  }
}
