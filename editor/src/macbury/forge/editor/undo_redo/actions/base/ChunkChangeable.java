package macbury.forge.editor.undo_redo.actions.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import macbury.forge.editor.undo_redo.actions.BlockSave;
import macbury.forge.editor.undo_redo.actions.TerrainCursorChangeable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 27.10.15.
 */
public abstract class ChunkChangeable extends TerrainCursorChangeable {
  private static final String TAG = "ChunkChangeable";
  private ChunkMap chunkMap;
  private Array<ChunkState> chunkStates;

  public ChunkChangeable(ChunkMap map) {
    super(map);
    this.chunkMap = map;
    chunkStates   = new Array<ChunkState>();
  }


  protected void saveChunkForCursor(Vector3i cursor) {
    Vector3i chunkPosition = chunkMap.voxelPositionToChunkPosition(cursor.x, cursor.y, cursor.z).cpy();
    if (!haveStateForThisCursor(chunkPosition)) {
      //Gdx.app.log(TAG, "Saving chunk: " + chunkPosition.toString());
      Chunk chunk = chunkMap.findByChunkPosition(chunkPosition);
      if (chunk != null && !chunk.isEmpty()) {
        chunkStates.add(new ChunkState(chunkMap, chunk));
      }
    }
  }

  private boolean haveStateForThisCursor(Vector3i cursor) {
    for (int i = 0; i < chunkStates.size; i++) {
      if (chunkStates.get(i).position.equals(cursor)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void revert() {
    for (ChunkState state : chunkStates) {
      //Gdx.app.log(TAG, "Restoring chunk: " + state.position.toString());
      state.restore(chunkMap);
      chunkMap.rebuildChunkForChunkPositionIfExists(state.position);
      state.dispose();
    }

    chunkStates.clear();
    System.gc();
  }

  @Override
  public void dispose() {
    super.dispose();
    chunkMap = null;
    for (ChunkState state : chunkStates) {
      state.dispose();
    }
    chunkStates.clear();
    chunkStates = null;
  }

  public class ChunkState implements Disposable {
    private Vector3i start;
    public Vector3i position;
    protected Voxel[][][] blockSaves;

    public ChunkState(ChunkMap map, Chunk chunk) {
      this.blockSaves     = new Voxel[ChunkMap.CHUNK_SIZE][ChunkMap.CHUNK_SIZE][ChunkMap.CHUNK_SIZE];
      Vector3i tempCursor = new Vector3i();
      for (int x = 0; x < ChunkMap.CHUNK_SIZE; x++) {
        for (int z = 0; z < ChunkMap.CHUNK_SIZE; z++) {
          for (int y = 0; y < ChunkMap.CHUNK_SIZE; y++) {
            tempCursor.set(x,y,z).add(chunk.start);
            Voxel voxel = map.getVoxelForPosition(tempCursor);


            if (voxel != null)
              blockSaves[x][y][z] = new Voxel(voxel);
          }
        }
      }

      this.position   = new Vector3i(chunk.position);
      this.start      = new Vector3i(chunk.start);
    }

    public void restore(ChunkMap map) {
      Vector3i tempVec = new Vector3i();

      for (int x = 0; x < ChunkMap.CHUNK_SIZE; x++) {
        for (int z = 0; z < ChunkMap.CHUNK_SIZE; z++) {
          for (int y = 0; y < ChunkMap.CHUNK_SIZE; y++) {
            tempVec.set(x,y,z).add(start);
            if (blockSaves[x][y][z] != null) {
              map.setQuietVoxelForPosition(blockSaves[x][y][z], tempVec);
            } else {
              map.removeQuietVoxelForPosition(tempVec);
            }
          }
        }
      }

      clear();
    }

    private void clear() {
      for (int x = 0; x < ChunkMap.CHUNK_SIZE; x++) {
        for (int z = 0; z < ChunkMap.CHUNK_SIZE; z++) {
          for (int y = 0; y < ChunkMap.CHUNK_SIZE; y++) {
            blockSaves[x][y][z] = null;
          }
        }
      }
    }

    @Override
    public void dispose() {
      clear();
      start      = null;
      blockSaves = null;
      position = null;
      blockSaves = null;
    }
  }
}
