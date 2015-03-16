package macbury.forge.storage.serializers.level;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.ForgE;
import macbury.forge.level.LevelEnv;
import macbury.forge.level.LevelState;

/**
 * Created by macbury on 09.03.15.
 */
public class LevelStateBasicInfoSerializer extends Serializer<LevelState> {
  @Override
  public void write(Kryo kryo, Output output, LevelState object) {
    output.writeInt(object.getId());
    output.writeString(object.getName());

    output.writeInt(object.getWidth());
    output.writeInt(object.getHeight());
    output.writeInt(object.getDepth());

    kryo.writeObject(output, object.env);
  }

  @Override
  public LevelState read(Kryo kryo, Input input, Class<LevelState> type) {
    LevelState state = new LevelState(ForgE.db);
    state.setId(input.readInt());
    state.setName(input.readString());

    state.setWidth(input.readInt());
    state.setHeight(input.readInt());
    state.setDepth(input.readInt());
    state.env = kryo.readObject(input, LevelEnv.class);

    return state;
  }
}
