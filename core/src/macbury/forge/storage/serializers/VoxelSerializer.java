package macbury.forge.storage.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.blocks.Block;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 10.03.15.
 */
public class VoxelSerializer extends Serializer<Voxel> {
  @Override
  public void write(Kryo kryo, Output output, Voxel object) {
    output.writeByte(object.blockId);
    kryo.writeObjectOrNull(output, object.alginTo, Block.Side.class);
  }

  @Override
  public Voxel read(Kryo kryo, Input input, Class<Voxel> type) {
    Voxel voxel   = new Voxel();
    voxel.blockId = input.readByte();
    voxel.alginTo = kryo.readObjectOrNull(input, Block.Side.class);
    return voxel;
  }
}
