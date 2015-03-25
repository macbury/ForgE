package macbury.forge.storage.serializers.db.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.db.models.PlayerStartPosition;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 24.03.15.
 */
public class PlayerStartPositionSerializer extends Serializer<PlayerStartPosition> {
  @Override
  public void write(Kryo kryo, Output output, PlayerStartPosition object) {
    kryo.writeObject(output, object.voxelPosition);
    output.writeInt(object.mapId, true);
  }

  @Override
  public PlayerStartPosition read(Kryo kryo, Input input, Class<PlayerStartPosition> type) {
    PlayerStartPosition startPosition = new PlayerStartPosition();
    startPosition.voxelPosition       = kryo.readObject(input, Vector3i.class);
    startPosition.mapId               = input.readInt();
    return startPosition;
  }
}
