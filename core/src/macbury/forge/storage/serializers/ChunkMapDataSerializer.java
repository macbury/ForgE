package macbury.forge.storage.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 09.03.15.
 */
public class ChunkMapDataSerializer extends Serializer<ChunkMap> {
  @Override
  public void write(Kryo kryo, Output output, ChunkMap object) {
    output.writeInt(object.getCountChunksX());
    output.writeInt(object.getCountChunksY());
    output.writeInt(object.getCountChunksZ());
  }

  @Override
  public ChunkMap read(Kryo kryo, Input input, Class<ChunkMap> type) {
    return null;
  }
}
