package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.components.BoundBox;
import macbury.forge.components.Position;
import macbury.forge.components.Renderable;
import macbury.forge.components.Visible;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.shaders.utils.RenderableBaseShader;

import java.util.HashMap;

/**
 * Created by macbury on 19.10.14.
 */
public class TerrainSystem extends IntervalSystem implements Disposable {
  private static final String TERRAIN_SHADER = "terrain";
  private static final float UPDATE_EVERY    = 0.10f;//second
  private ComponentMapper<Renderable> rm = ComponentMapper.getFor(Renderable.class);
  private ComponentMapper<Visible>    vm = ComponentMapper.getFor(Visible.class);
  private ComponentMapper<BoundBox>  bbm = ComponentMapper.getFor(BoundBox.class);

  private final LevelEntityEngine engine;
  private HashMap<Chunk, Entity> tileEntities;
  private TerrainBuilder builder;
  private ChunkMap map;
  private int cursor = 0;

  public TerrainSystem(LevelEntityEngine engine) {
    super(UPDATE_EVERY);
    this.engine  = engine;
    tileEntities = new HashMap<Chunk, Entity>();

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
        Visible     visibleComponent;
        BoundBox    boundBox;

        if (tileEntities.containsKey(chunk)) {
          tileEntity              = tileEntities.get(chunk);
          renderableComponent     = rm.get(tileEntity);
          visibleComponent        = vm.get(tileEntity);
          boundBox                = bbm.get(tileEntity);

          clearMeshForEntity(tileEntity);
        } else {
          tileEntity                                         = engine.createEntity();

          boundBox                                           = engine.createComponent(BoundBox.class);
          renderableComponent                                = engine.createComponent(Renderable.class);
          visibleComponent                                   = engine.createComponent(Visible.class);

          renderableComponent.instance                       = new TerrainChunkRenderable();
          renderableComponent.instance.primitiveType         = GL30.GL_TRIANGLES;
          renderableComponent.instance.shader                = (RenderableBaseShader)ForgE.shaders.get(TERRAIN_SHADER);
          tileEntities.put(chunk, tileEntity);

          tileEntity.add(boundBox);
          tileEntity.add(visibleComponent);
          tileEntity.add(renderableComponent);
          tileEntity.add(engine.createComponent(Position.class));
          engine.addEntity(tileEntity);
        }

        visibleComponent.visible = true;
        if (ForgE.config.generateWireframe)
          renderableComponent.instance.wireframe           = builder.wireframe();
        renderableComponent.instance.mesh                  = builder.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.Color);
        renderableComponent.instance.mesh.calculateBoundingBox(boundBox.box);
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
    Visible  visibleComponent      = vm.get(tileEntity);
    if (renderableComponent.instance.mesh != null) {
      renderableComponent.instance.mesh.dispose();
    }
    visibleComponent.visible = false;
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

}
