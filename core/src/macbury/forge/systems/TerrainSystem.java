package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.components.Position;
import macbury.forge.components.Renderable;
import macbury.forge.graphics.ColorMaterial;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.level.map.ChunkMap;

import java.util.HashMap;

/**
 * Created by macbury on 19.10.14.
 */
public class TerrainSystem extends EntitySystem implements Disposable {
  private ComponentMapper<Renderable> rm = ComponentMapper.getFor(Renderable.class);
  private final Vector3 tempA;
  private final LevelEntityEngine engine;
  private HashMap<Chunk, Entity> tileEntities;
  private TerrainBuilder builder;
  private ChunkMap map;

  public TerrainSystem(LevelEntityEngine engine) {
    super();
    this.engine  = engine;
    tileEntities = new HashMap<Chunk, Entity>();
    this.tempA   = new Vector3();
  }

  @Override
  public void update(float deltaTime) {
    while(map.chunkToRebuild.size > 0) {
      Chunk chunk = map.chunkToRebuild.pop();
      buildChunk(chunk);
    }
  }

  private void buildChunk(Chunk chunk) {
    builder.begin(); {
      for (int x = chunk.start.x; x < chunk.end.x; x++) {
        for (int y = chunk.start.y; y < chunk.end.y; y++) {
          for (int z = chunk.start.z; z < chunk.end.z; z++) {
            if (map.isSolid(x, y, z)) {
              tempA.set(x,y,z);
              ColorMaterial material = map.getMaterialForPosition(x, y, z);

              if (map.isEmpty(x,y+1,z)) {
                builder.top(tempA, ChunkMap.TILE_SIZE, material);
              }

              if (map.isEmpty(x,y-1,z)) {
                builder.bottom(tempA, ChunkMap.TILE_SIZE, material);
              }

              if (map.isEmpty(x-1,y,z)) {
                builder.left(tempA, ChunkMap.TILE_SIZE, material);
              }

              if (map.isEmpty(x+1,y,z)) {
                builder.right(tempA, ChunkMap.TILE_SIZE, material);
              }

              if (map.isEmpty(x,y,z+1)) {
                builder.front(tempA, ChunkMap.TILE_SIZE, material);
              }

              if (map.isEmpty(x,y,z-1)) {
                builder.back(tempA, ChunkMap.TILE_SIZE, material);
              }
            }
          }
        }
      }

      if (builder.isEmpty()) {
        if (tileEntities.containsKey(chunk)) {
          removeEntityForChunk(chunk);
          tileEntities.remove(chunk);
        }
      } else {
        Entity tileEntity;
        Renderable renderableComponent;

        if (tileEntities.containsKey(chunk)) {
          tileEntity              = tileEntities.get(chunk);
          renderableComponent     = rm.get(tileEntity);
          clearMeshForEntity(tileEntity);
        } else {
          tileEntity                = engine.createEntity();
          renderableComponent       = engine.createComponent(Renderable.class);

          renderableComponent.instance                       = new TerrainChunkRenderable();
          renderableComponent.instance.primitiveType         = GL30.GL_TRIANGLES;
          renderableComponent.instance.shader                = ForgE.shaders.get("mesh_test");
          tileEntities.put(chunk, tileEntity);

          tileEntity.add(renderableComponent);
          tileEntity.add(engine.createComponent(Position.class));
          engine.addEntity(tileEntity);
        }

        if (ForgE.config.generateWireframe)
          renderableComponent.instance.wireframe           = builder.wireframe();
        renderableComponent.instance.mesh                  = builder.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Color);
        renderableComponent.visible                        = true;
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
    renderableComponent.visible = false;
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
