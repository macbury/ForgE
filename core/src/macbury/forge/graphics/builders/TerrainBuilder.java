package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.graphics.ColorMaterial;
import macbury.forge.graphics.VoxelMap;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.shaders.utils.RenderableBaseShader;
import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder extends VoxelsAssembler {
  private static final String TAG = "TerrainBuilder";
  private static final String TERRAIN_SHADER = "terrain";
  private final VoxelMap map;
  public final TerrainCursor cursor;
  private Vector3 tempA = new Vector3();

  public TerrainBuilder(VoxelMap voxelMap) {
    super();
    this.map       = voxelMap;
    this.cursor    = new TerrainCursor();
  }

  public void bottomFace() {
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int x = cursor.start.x; x < cursor.end.x; x++) {
        for (int z = cursor.start.z; z < cursor.end.z; z++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x,y-1,z)) {
              bottom(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }

    cursor.size.sub(cursor.size.x, cursor.size.y, cursor.size.z).add(1,1,1);
  }

  public void topFace() {
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int x = cursor.start.x; x < cursor.end.x; x++) {
        for (int z = cursor.start.z; z < cursor.end.z; z++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x,y+1,z)) {
              top(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }

    cursor.size.sub(cursor.size.x, cursor.size.y, cursor.size.z).add(1,1,1);
  }

  public void rightFace() {
    for (int z = cursor.start.z; z < cursor.end.z; z++) {
      for (int y = cursor.start.y; y < cursor.end.y; y++) {
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x+1,y,z)) {
              right(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }

    cursor.size.sub(cursor.size.x, cursor.size.y, cursor.size.z).add(1,1,1);
  }

  public void leftFace() {
    //Gdx.app.log(TAG, "Building faces for: " + cursor.start.toString() + " - " + cursor.end.toString());
    for (int z = cursor.start.z; z < cursor.end.z; z++) {
      for (int y = cursor.start.y; y < cursor.end.y; y++) {
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x-1,y,z)) {
              left(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }

    cursor.size.sub(cursor.size.x, cursor.size.y, cursor.size.z).add(1,1,1);
  }

  public void backFace() {
    //Gdx.app.log(TAG, "Building faces for: " + cursor.start.toString() + " - " + cursor.end.toString());
    for (int z = cursor.start.z; z < cursor.end.z; z++) {
      for (int y = cursor.start.y; y < cursor.end.y; y++) {
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x,y,z-1)) {
              back(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }

    cursor.size.sub(cursor.size.x, cursor.size.y, cursor.size.z).add(1,1,1);
  }

  public void frontFace() {
    //Gdx.app.log(TAG, "Building faces for: " + cursor.start.toString() + " - " + cursor.end.toString());
    for (int z = cursor.start.z; z < cursor.end.z; z++) {
      for (int y = cursor.start.y; y < cursor.end.y; y++) {
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempA.set(x,y,z);
            ColorMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x,y,z+1)) {
              front(tempA, ChunkMap.TILE_SIZE, material);
            }
          }
        }
      }
    }

    cursor.size.sub(cursor.size.x, cursor.size.y, cursor.size.z).add(1,1,1);
  }

  public void facesForChunk(Chunk chunk) {
    facesForPart(chunk.start, chunk.end, chunk.size);
  }

  private void facesForPart(Vector3Int start, Vector3Int end, Vector3 outSize) {
    outSize.setZero();
    Gdx.app.log(TAG, "Building faces for: " + start.toString() + " - " + end.toString());
    for (int x = start.x; x < end.x; x++) {
      for (int y = start.y; y < end.y; y++) {
        for (int z = start.z; z < end.z; z++) {
          if (map.isSolid(x, y, z)) {
            outSize.x = Math.max(x, outSize.x);
            outSize.y = Math.max(y, outSize.y);
            outSize.z = Math.max(z, outSize.z);

            tempA.set(x,y,z);
            Gdx.app.log(TAG, tempA.toString());
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
    outSize.sub(start.x, start.y, start.z).add(1,1,1);
  }

  public void facesForMap() {
    facesForPart(new Vector3Int(0,0,0), new Vector3Int(map.getWidth(), map.getHeight(), map.getDepth()), new Vector3());
  }

  public VoxelFaceRenderable getRenderable() {
    VoxelFaceRenderable renderable   = new VoxelFaceRenderable();
    renderable.primitiveType         = GL30.GL_TRIANGLES;
    renderable.shader                = (RenderableBaseShader) ForgE.shaders.get(TERRAIN_SHADER);
    if (ForgE.config.generateWireframe)
      renderable.wireframe           = this.wireframe();
    renderable.mesh                  = this.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.Color);

    return renderable;
  }


}
