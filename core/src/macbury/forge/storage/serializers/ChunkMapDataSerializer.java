package macbury.forge.storage.serializers;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 09.03.15.
 */
public class ChunkMapDataSerializer extends Serializer<ChunkMap> {
  private static final String TAG = "ChunkMapDataSerializer";
  private Vector3i tempA = new Vector3i();
  @Override
  public void write(Kryo kryo, Output output, ChunkMap object) {
    output.writeInt(object.getWidth());
    output.writeInt(object.getHeight());
    output.writeInt(object.getDepth());

    synchronized (object) {
      for (int x = 0; x < object.getWidth(); x++) {
        for (int y = 0; y < object.getHeight(); y++) {
          for (int z = 0; z < object.getDepth(); z++) {
            Voxel voxel = object.getVoxelForPosition(x,y,z);
            kryo.writeObjectOrNull(output, voxel, Voxel.class);
          }
        }
      }
    }
  }

  @Override
  public ChunkMap read(Kryo kryo, Input input, Class<ChunkMap> type) {
    ChunkMap map = ChunkMap.build();

    int width  = input.readInt();
    int height = input.readInt();
    int depth  = input.readInt();
    map.initialize(width, height, depth);

    for (int x = 0; x < map.getWidth(); x++) {
      for (int y = 0; y < map.getHeight(); y++) {
        for (int z = 0; z < map.getDepth(); z++) {
          tempA.set(x,y,z);
          Voxel voxel = kryo.readObjectOrNull(input, Voxel.class);
          map.setQuietVoxelForPosition(voxel, tempA);
        }
      }
    }

    map.splitIntoChunks();
    map.rebuildAll();
    return map;
  }
}
