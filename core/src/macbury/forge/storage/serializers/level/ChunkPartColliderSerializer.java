package macbury.forge.storage.serializers.level;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.builders.ChunkPartCollider;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 05.08.15.
 */
public class ChunkPartColliderSerializer extends Serializer<ChunkPartCollider> {
  @Override
  public void write(Kryo kryo, Output output, ChunkPartCollider collider) {
    kryo.writeObject(output, collider.voxel);
    kryo.writeObject(output, collider.side);
    kryo.writeObject(output, collider.position);
    kryo.writeObject(output, collider.size);
  }

  @Override
  public ChunkPartCollider read(Kryo kryo, Input input, Class<ChunkPartCollider> type) {
    Voxel voxel                = kryo.readObject(input, Voxel.class);
    Block.Side side            = kryo.readObject(input, Block.Side.class);
    ChunkPartCollider collider = new ChunkPartCollider(voxel, side);
    collider.position.set(kryo.readObject(input, Vector3i.class));
    collider.size.set(kryo.readObject(input, Vector3i.class));
    return collider;
  }
}
