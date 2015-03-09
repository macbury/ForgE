package macbury.forge.storage.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.blocks.Block;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 09.03.15.
 */
public class ChunkMapDataSerializer extends Serializer<ChunkMap> {
  @Override
  public void write(Kryo kryo, Output output, ChunkMap object) {
    output.writeInt(object.getWidth());
    output.writeInt(object.getHeight());
    output.writeInt(object.getDepth());

    for (int x = 0; x < object.getWidth(); x++) {
      for (int y = 0; y < object.getHeight(); y++) {
        for (int z = 0; z < object.getDepth(); z++) {
          Block block = object.getBlockForPosition(x,y,z);
          output.writeByte(block.id);
        }
      }
    }
  }

  @Override
  public ChunkMap read(Kryo kryo, Input input, Class<ChunkMap> type) {
    return null;
  }
}
