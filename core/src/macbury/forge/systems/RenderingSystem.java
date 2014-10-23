package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import macbury.forge.components.Position;
import macbury.forge.components.Renderable;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.level.Level;

/**
 * Created by macbury on 19.10.14.
 */
public class RenderingSystem extends IteratingSystem {
  private ComponentMapper<Position>   pm = ComponentMapper.getFor(Position.class);
  private ComponentMapper<Renderable> rm = ComponentMapper.getFor(Renderable.class);
  private VoxelBatch batch;

  public RenderingSystem(Level level) {
    super(Family.getFor(Position.class, Renderable.class));

    this.batch = level.batch;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    Position position     = pm.get(entity);
    Renderable renderable = rm.get(entity);

    if (position.visible) {
      BaseRenderable baseRenderable = renderable.instance;
      if (renderable.useWorldTransform) {
        baseRenderable.worldTransform.set(position.getWorldTransformMatrix());
      }

      batch.add(baseRenderable);
    }
  }
}
