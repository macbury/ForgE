package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.graphics.ColorMaterial;
import macbury.forge.graphics.VoxelMap;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.level.map.ChunkMap;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder extends VoxelsAssembler {
  private final VoxelMap map;
  private Vector3 tempA = new Vector3();

  public TerrainBuilder(VoxelMap voxelMap) {
    super();
    this.map       = voxelMap;
  }

  public void facesForChunk(Chunk chunk) {
    for (int x = chunk.start.x; x < chunk.end.x; x++) {
      for (int y = chunk.start.y; y < chunk.end.y; y++) {
        for (int z = chunk.start.z; z < chunk.end.z; z++) {
          if (map.isSolid(x, y, z)) {
            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x,y+1,z)) {
              top(tempA, ChunkMap.TILE_SIZE, material);
            }

            if (map.isEmpty(x,y-1,z)) {
              bottom(tempA, ChunkMap.TILE_SIZE, material);
            }

            if (map.isEmpty(x-1,y,z)) {
              left(tempA, ChunkMap.TILE_SIZE, material);
            }

            if (map.isEmpty(x+1,y,z)) {
              right(tempA, ChunkMap.TILE_SIZE, material);
            }

            if (map.isEmpty(x,y,z+1)) {
              front(tempA, ChunkMap.TILE_SIZE, material);
            }

            if (map.isEmpty(x,y,z-1)) {
              back(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }
  }

  public TerrainChunkRenderable getRenderable() {
    TerrainChunkRenderable renderable = new TerrainChunkRenderable();
    renderable.primitiveType         = GL30.GL_TRIANGLES;
    renderable.mesh                  = mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Color);
    renderable.shader                = ForgE.shaders.get("mesh_test");
    if (ForgE.config.generateWireframe)
      renderable.wireframe           = wireframe();
    return renderable;
  }
}
