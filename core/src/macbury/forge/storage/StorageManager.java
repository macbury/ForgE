package macbury.forge.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.db.GameDatabase;
import macbury.forge.storage.serializers.GameDatabaseSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by macbury on 07.11.14.
 */
public class StorageManager extends Kryo {
  public StorageManager() {
    super();
    register(GameDatabase.class, new GameDatabaseSerializer());
  }

  public GameDatabase loadOrInitializeDB() {
    FileHandle dbFile = Gdx.files.internal(GameDatabase.FILE_NAME);
    GameDatabase db   = null;
    if (dbFile.exists()) {
      db = readObject(new Input(dbFile.read()), GameDatabase.class);
    } else {
      db = new GameDatabase();
      db.bootstrap();
      saveDB(db);
    }

    return db;
  }

  public void saveDB(GameDatabase db) {
    FileHandle dbFile = Gdx.files.internal(GameDatabase.FILE_NAME);
    try {
      Output output = new Output(new FileOutputStream(dbFile.file(), false));
      writeObject(output, db);
      output.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
