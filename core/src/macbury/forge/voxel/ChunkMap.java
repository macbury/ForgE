package macbury.forge.voxel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 19.10.14.
 */
public class ChunkMap extends VoxelMap {

  public static final int CHUNK_SIZE              = 20;
  public static final int CHUNK_ARRAY_SIZE        = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;
  public static final Vector3 TERRAIN_TILE_SIZE   = new Vector3(1,1,1);
  private static final String TAG = "ChunkMap";
  public final Array<Chunk> chunks;
  public final Array<Chunk> chunkToRebuild;

  private int countChunksX;
  private int countChunksY;
  private int countChunksZ;
  private Vector3i tempA = new Vector3i();
  private Vector3i tempB = new Vector3i();

  public ChunkMap(Vector3 tileSize, BlocksProvider blocksProvider) {
    super(tileSize, blocksProvider);
    chunks                    = new Array<Chunk>();
    chunkToRebuild            = new Array<Chunk>();
  }

  public void buildFloor() {
    ForgE.log(TAG, "Building floor");
    Block mainBlock = blocks.find(1);
    for(int y = 0; y < 1; y++) {
      for(int x = 0; x < width; x++) {
        for(int z = 0; z < depth; z++) {
          setBlockIdForPosition(mainBlock.id, x, y, z);
        }
      }
    }
    ForgE.log(TAG, "Builded all floor");
  }

  public Vector3i voxelPositionToChunkPosition(int x, int y, int z){
    return tempA.set(x/CHUNK_SIZE,y/CHUNK_SIZE,z/CHUNK_SIZE);
  }

  public Vector3i worldPositionToVoxelPosition(Vector3 worldPosition){
    return tempA.set(MathUtils.floor(worldPosition.x), MathUtils.floor(worldPosition.y), MathUtils.floor(worldPosition.z));
  }


  public Chunk findByChunkPosition(Vector3i position) {
    for (Chunk chunk : chunks) {
      if (chunk.position.equals(position))
        return chunk;
    }
    return null;
  }

  public void setQuickBlockIdForPosition(byte blockId, int x, int y, int z) {
    setBlockIdForPosition(blockId, x, y, z);
  }

  @Override
  public Voxel setBlockIdForPosition(byte blockId, int x, int y, int z) {
    Voxel voxel = super.setBlockIdForPosition(blockId, x, y, z);
    rebuildChunkAroundPosition(x, y, z);
    return voxel;
  }

  @Override
  public void setBlockForPosition(Block block, int x, int y, int z) {
    super.setBlockForPosition(block, x, y, z);
    rebuildChunkAroundPosition(x, y, z);
  }

  private void rebuildChunkAroundPosition(int x, int y, int z) {
    Vector3i chunkPosition = voxelPositionToChunkPosition(x, y, z);
    Chunk    centerChunk   = findByChunkPosition(chunkPosition);

    if (centerChunk == null) {
      return;
    }

    addToRebuild(centerChunk);

    if (centerChunk.start.x == x) {
      //ForgE.log(TAG, "X left border!");
      tempB.set(chunkPosition).x -= 1;
      rebuildChunkForChunkPositionIfExists(tempB);
    }

    if (centerChunk.end.x - 1 == x) {
      //ForgE.log(TAG, "X right border!");
      tempB.set(chunkPosition).x += 1;
      rebuildChunkForChunkPositionIfExists(tempB);
    }

    if (centerChunk.start.y == y) {
      //ForgE.log(TAG, "Y top border!");
      tempB.set(chunkPosition).y += 1;
      rebuildChunkForChunkPositionIfExists(tempB);
    }

    if (centerChunk.end.y - 1 == y) {
      //ForgE.log(TAG, "Y bottom border!");
      tempB.set(chunkPosition).y += 1;
      rebuildChunkForChunkPositionIfExists(tempB);
    }

    if (centerChunk.start.z == z) {
      //ForgE.log(TAG, "Z Front border!");
      tempB.set(chunkPosition).z -= 1;
      rebuildChunkForChunkPositionIfExists(tempB);
    }

    if (centerChunk.end.z - 1 == z) {
      //ForgE.log(TAG, "Z back border!");
      tempB.set(chunkPosition).z += 1;
      rebuildChunkForChunkPositionIfExists(tempB);
    }
  }

  public void rebuildChunkForChunkPositionIfExists(Vector3i chunkPosition) {
    Chunk chunk            = findByChunkPosition(chunkPosition);
    if (chunk != null) {
      addToRebuild(chunk);
    }
  }

  private void rebuildChunkForChunkPosition(Vector3i chunkPosition) {
    Chunk chunk            = findByChunkPosition(chunkPosition);
    if (chunk == null) {
      throw new GdxRuntimeException("Chunk is null!!");
    } else {
      addToRebuild(chunk);
    }
  }

  private void rebuildChunkForPosition(int x, int y, int z) {
    Vector3i chunkPosition = voxelPositionToChunkPosition(x,y,z);
    rebuildChunkForChunkPosition(chunkPosition);
  }

  public void setQuietVoxelForPosition(Voxel voxel, Vector3i voxelPosition) {
    super.setVoxelForPosition(voxel, voxelPosition);
  }

  @Override
  public void setVoxelForPosition(Voxel voxel, Vector3i voxelPosition) {
    super.setVoxelForPosition(voxel, voxelPosition);
    rebuildChunkAroundPosition(voxelPosition.x, voxelPosition.y, voxelPosition.z);
  }

  @Override
  public void setEmptyForPosition(int x, int y, int z) {
    super.setEmptyForPosition(x, y, z);
    rebuildChunkAroundPosition(x, y, z);
  }

  public void splitIntoChunks() {
    ForgE.log(TAG, "Splitting into chunks");
    chunks.clear();
    this.countChunksX = width / CHUNK_SIZE;
    this.countChunksY = height / CHUNK_SIZE;
    this.countChunksZ = depth / CHUNK_SIZE;
    for(int chunkX = 0; chunkX < countChunksX; chunkX++) {
      for(int chunkY = 0; chunkY < countChunksY; chunkY++) {
        for(int chunkZ = 0; chunkZ < countChunksZ; chunkZ++) {
          Chunk chunk  = new Chunk();
          chunk.position.set(chunkX, chunkY, chunkZ);
          chunk.worldPosition.set(chunkX * CHUNK_SIZE, chunkY * CHUNK_SIZE, chunkZ * CHUNK_SIZE).scl(voxelSize);
          chunk.start.set(chunkX * CHUNK_SIZE, chunkY * CHUNK_SIZE, chunkZ * CHUNK_SIZE);
          chunk.end.set(chunk.start).add(CHUNK_SIZE, CHUNK_SIZE, CHUNK_SIZE);
          chunk.size.set(CHUNK_SIZE, CHUNK_SIZE, CHUNK_SIZE).scl(voxelSize);
          chunks.add(chunk);
        }
      }
    }
    ForgE.log(TAG, "Total chunks: " + chunks.size);
  }

  private void addToRebuild(Chunk chunk) {
    if (chunkToRebuild.indexOf(chunk, true) == -1){
      chunkToRebuild.add(chunk);
    }
  }

  public void findChunksWithData(Array<Chunk> out) {
    out.clear();
    for(Chunk chunk : chunks) {
      if (chunk.renderables.size > 0 || isAnySolidVoxelsInChunk(chunk)) {
        out.add(chunk);
      }
    }
  }

  public boolean isAnySolidVoxelsInChunk(Chunk chunk) {
    for (int x = chunk.start.x; x < chunk.end.x; x++) {
      for (int y = chunk.start.y; y < chunk.end.y; y++) {
        for (int z = chunk.start.z; z < chunk.end.z; z++) {
          tempA.set(x,y,z);
          if (isNotAir(tempA)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public void dispose() {
    super.dispose();
    for(Chunk chunk : chunks) {
      chunk.dispose();
    }
    chunks.clear();
    chunkToRebuild.clear();
  }

  public void rebuildAll() {
    chunkToRebuild.addAll(chunks);
  }

  public int getCountChunksX() {
    return countChunksX;
  }

  public void setCountChunksX(int countChunksX) {
    this.countChunksX = countChunksX;
  }

  public int getCountChunksY() {
    return countChunksY;
  }

  public void setCountChunksY(int countChunksY) {
    this.countChunksY = countChunksY;
  }

  public int getCountChunksZ() {
    return countChunksZ;
  }

  public void setCountChunksZ(int countChunksZ) {
    this.countChunksZ = countChunksZ;
  }

  public static ChunkMap build() {
    return new ChunkMap(ChunkMap.TERRAIN_TILE_SIZE, ForgE.blocks);
  }

}
