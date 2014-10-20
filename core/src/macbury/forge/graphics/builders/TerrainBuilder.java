package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.ColorMaterial;
import macbury.forge.graphics.VoxelMap;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.utils.Vector3Int;

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
    facesForPart(chunk.start, chunk.end);
  }


  private void facesForPart(Vector3Int start, Vector3Int end) {
    for (int x = start.x; x < end.x; x++) {
      for (int y = start.y; y < end.y; y++) {
        for (int z = start.z; z < end.z; z++) {
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

  public void facesForMap() {
    facesForPart(new Vector3Int(0,0,0), new Vector3Int(map.getWidth(), map.getHeight(), map.getDepth()));
  }
}
