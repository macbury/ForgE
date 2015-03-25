package macbury.forge.storage.serializers.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

import java.util.ArrayList;

/**
 * Created by macbury on 09.03.15.
 */
public class ChunkMapDataSerializer extends Serializer<ChunkMap> {
  private static final String TAG = "ChunkMapDataSerializer";
  private Vector3i tempA = new Vector3i();
  private final static int VERSION = 2;
  private Array<Chunk> tempChunks = new Array<Chunk>();
  @Override
  public void write(Kryo kryo, Output output, ChunkMap object) {
    output.writeInt(VERSION);
    output.writeInt(object.getWidth());
    output.writeInt(object.getHeight());
    output.writeInt(object.getDepth());

    synchronized (object) {
      object.findChunksWithData(tempChunks);
      output.writeInt(tempChunks.size, true);

      for(Chunk chunk : tempChunks) {
        kryo.writeObject(output, chunk.position);
        kryo.writeObject(output, chunk.start);
        kryo.writeObject(output, chunk.end);
        for (int x = chunk.start.x; x < chunk.end.x; x++) {
          for (int y = chunk.start.y; y < chunk.end.y; y++) {
            for (int z = chunk.start.z; z < chunk.end.z; z++) {
              tempA.set(x,y,z);
              Voxel voxel = object.getVoxelForPosition(x,y,z);
              kryo.writeObjectOrNull(output, voxel, Voxel.class);
            }
          }
        }
      }
    }

    tempChunks.clear();
  }

  @Override
  public ChunkMap read(Kryo kryo, Input input, Class<ChunkMap> type) {
    ChunkMap map = ChunkMap.build();

    int version = input.readInt();
    if (version != VERSION) {
      throw new GdxRuntimeException("Map version is: " + version + " but current supported version is: " + VERSION);
    }

    int width  = input.readInt();
    int height = input.readInt();
    int depth  = input.readInt();
    map.initialize(width, height, depth);

    int chunksToLoad = input.readInt(true);
    map.splitIntoChunks();
    for (int i = 0; i < chunksToLoad; i++) {
      Vector3i chunkPosition = kryo.readObject(input, Vector3i.class);
      Vector3i start         = kryo.readObject(input, Vector3i.class);
      Vector3i end           = kryo.readObject(input, Vector3i.class);

      map.rebuildChunkForChunkPositionIfExists(chunkPosition);

      for (int x = start.x; x < end.x; x++) {
        for (int y = start.y; y < end.y; y++) {
          for (int z = start.z; z < end.z; z++) {
            tempA.set(x,y,z);
            Voxel voxel = kryo.readObjectOrNull(input, Voxel.class);
            map.setQuietVoxelForPosition(voxel, tempA);
          }
        }
      }
    }

    tempChunks.clear();
    return map;
  }
}
