package macbury.forge.storage.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.db.GameDatabase;

/**
 * Created by macbury on 10.11.14.
 */
public class GameDatabseSerializer extends Serializer<GameDatabase> {
  @Override
  public void write(Kryo kryo, Output output, GameDatabase gameDatabase) {
    output.writeInt(gameDatabase.currentyUID);
    output.writeString(gameDatabase.title);
  }

  @Override
  public GameDatabase read(Kryo kryo, Input input, Class<GameDatabase> type) {
    GameDatabase db = new GameDatabase();
    db.bootstrap();
    db.currentyUID = input.readInt();
    db.title       = input.readString();
    return db;
  }

}
