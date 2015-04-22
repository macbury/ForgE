package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import macbury.forge.ForgE;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RenderableComponent;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.level.LevelEnv;
import macbury.forge.terrain.TerrainEngine;

/**
 * Created by macbury on 19.10.14.
 */
public class WorldRenderingSystem extends IteratingSystem {
  private final GameCamera camera;
  private final LevelEnv env;
  private final TerrainEngine terrain;
  private ComponentMapper<PositionComponent>   pm = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<RenderableComponent> rm = ComponentMapper.getFor(RenderableComponent.class);
  private VoxelBatch batch;

  public WorldRenderingSystem(Level level) {
    super(Family.getFor(PositionComponent.class, RenderableComponent.class));

    this.terrain = level.terrainEngine;
    this.batch   = level.batch;
    this.env     = level.env;
    this.camera  = level.camera;
  }

  @Override
  public void update(float deltaTime) {
    ForgE.graphics.clearAll(env.skyColor);
    batch.begin(camera); {
      batch.add(terrain);
      super.update(deltaTime);
      batch.render(env);
    } batch.end();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    PositionComponent position     = pm.get(entity);
    RenderableComponent renderable = rm.get(entity);

    if (position.visible) {
      BaseRenderable baseRenderable = renderable.instance;
      if (renderable.useWorldTransform) {
        baseRenderable.worldTransform.set(position.worldTransform);
      }

      batch.add(baseRenderable);
    }
  }
}
