package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.components.Position;
import macbury.forge.components.Renderable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.BaseRenderableProvider;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.systems.engine.LevelEntityEngine;

import java.util.HashMap;

/**
 * Created by macbury on 19.10.14.
 */
public class TerrainSystem extends IntervalSystem implements Disposable, BaseRenderableProvider {
  private static final float UPDATE_EVERY    = 0.10f;//second
  private ComponentMapper<Renderable> rm     = ComponentMapper.getFor(Renderable.class);
  private ComponentMapper<Position>   pm     = ComponentMapper.getFor(Position.class);
  private final LevelEntityEngine engine;
  private HashMap<Chunk, Entity> tileEntities;
  private TerrainBuilder builder;
  private ChunkMap map;
  private BoundingBox tempBox;
  private int cursor = 0;

  public TerrainSystem(LevelEntityEngine engine) {
    super(UPDATE_EVERY);
    this.engine  = engine;
    tileEntities = new HashMap<Chunk, Entity>();
    tempBox      = new BoundingBox(Vector3.Zero, Vector3.Zero);
  }

  @Override
  protected void updateInterval() {
    if (map.chunkToRebuild.size > 0) {
      cursor = (int)Math.ceil(map.chunkToRebuild.size * UPDATE_EVERY);
      while (map.chunkToRebuild.size > 0) {
        Chunk chunk = map.chunkToRebuild.pop();
        buildChunkGeometry(chunk);
        cursor--;
        if (cursor <= 0) {
          break;
        }
      }
    }
  }

  private void buildChunkGeometry(Chunk chunk) {
    builder.begin(); {
      builder.facesForChunk(chunk);

      if (builder.isEmpty()) {
        if (tileEntities.containsKey(chunk)) {
          removeEntityForChunk(chunk);
          tileEntities.remove(chunk);
        }
      } else {
        Entity      tileEntity;
        Renderable  renderableComponent;
        Position    positionComponent;
        if (tileEntities.containsKey(chunk)) {
          tileEntity              = tileEntities.get(chunk);
          renderableComponent     = rm.get(tileEntity);
          positionComponent       = pm.get(tileEntity);
          clearMeshForEntity(tileEntity);
        } else {
          tileEntity                                         = engine.createEntity();
          positionComponent                                  = engine.createComponent(Position.class);
          renderableComponent                                = engine.createComponent(Renderable.class);
          renderableComponent.useWorldTransform              = false;
          renderableComponent.instance                       = new TerrainChunkRenderable();
          renderableComponent.instance.primitiveType         = GL30.GL_TRIANGLES;
          tileEntities.put(chunk, tileEntity);

          tileEntity.add(renderableComponent);
          tileEntity.add(positionComponent);
          engine.addEntity(tileEntity);
        }

        if (ForgE.config.generateWireframe)
          renderableComponent.instance.wireframe           = builder.wireframe();
        renderableComponent.instance.mesh                  = builder.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.Color);
        positionComponent.vector.set(chunk.worldPosition);
        positionComponent.size.set(chunk.size);
        positionComponent.visible = true;
      }
    } builder.end();
  }

  public void setMap(ChunkMap map) {
    this.map     = map;
    this.builder = new TerrainBuilder(map);
  }

  private void removeEntityForChunk(Chunk chunk) {
    Entity tileEntity              = tileEntities.get(chunk);
    clearMeshForEntity(tileEntity);
    engine.removeEntity(tileEntity);
  }

  private void clearMeshForEntity(Entity tileEntity) {
    Renderable renderableComponent = rm.get(tileEntity);
    if (renderableComponent.instance.mesh != null) {
      renderableComponent.instance.mesh.dispose();
    }
  }

  @Override
  public void dispose() {
    for (Chunk chunk : tileEntities.keySet()) {
      removeEntityForChunk(chunk);
    }
    map     = null;
    builder.dispose();
    builder = null;
  }

  @Override
  public void getRenderables(Array<BaseRenderable> renderables) {

  }
}
