package macbury.forge.storage.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.db.GameDatabase;

/**
 * Created by macbury on 10.11.14.
 */
public class GameDatabaseSerializer extends Serializer<GameDatabase> {
  @Override
  public void write(Kryo kryo, Output output, GameDatabase gameDatabase) {
    output.writeInt(gameDatabase.currentyUID);
    output.writeString(gameDatabase.title);
    output.writeLong(gameDatabase.build++);
  }

  @Override
  public GameDatabase read(Kryo kryo, Input input, Class<GameDatabase> type) {
    GameDatabase db = new GameDatabase();
    db.bootstrap();
    db.currentyUID = input.readInt();
    db.title       = input.readString();
    db.build       = input.readLong();
    return db;
  }

}
