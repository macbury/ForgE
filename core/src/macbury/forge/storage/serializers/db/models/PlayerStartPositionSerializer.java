package macbury.forge.storage.serializers.db.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.db.models.Teleport;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 24.03.15.
 */
public class PlayerStartPositionSerializer extends Serializer<Teleport> {
  @Override
  public void write(Kryo kryo, Output output, Teleport object) {
    kryo.writeObject(output, object.voxelPosition);
    output.writeInt(object.mapId, true);
  }

  @Override
  public Teleport read(Kryo kryo, Input input, Class<Teleport> type) {
    Teleport startPosition = new Teleport(kryo.readObject(input, Vector3i.class), input.readInt(true));
    return startPosition;
  }
}
