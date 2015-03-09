package macbury.forge.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import macbury.forge.db.GameDatabase;
import macbury.forge.level.LevelState;
import macbury.forge.storage.serializers.ChunkMapDataSerializer;
import macbury.forge.storage.serializers.GameDatabaseSerializer;
import macbury.forge.storage.serializers.FullLevelStateSerializer;
import macbury.forge.storage.serializers.LevelStateBasicInfoSerializer;
import macbury.forge.voxel.ChunkMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by macbury on 07.11.14.
 */
public class StorageManager {
  public static final String TAG = "StorageManager";
  public final KryoPool pool;

  private KryoFactory factory = new KryoFactory() {
    public Kryo create () {
      Kryo kryo = new Kryo();
      kryo.register(GameDatabase.class, new GameDatabaseSerializer());
      kryo.register(LevelState.class, new LevelStateBasicInfoSerializer());
      kryo.register(LevelState.class, new FullLevelStateSerializer());
      kryo.register(ChunkMap.class, new ChunkMapDataSerializer());
      return kryo;
    }
  };

  public StorageManager() {
    super();
    this.pool = new KryoPool.Builder(factory).softReferences().build();
  }

  public GameDatabase loadOrInitializeDB() {
    FileHandle dbFile = Gdx.files.internal(GameDatabase.FILE_NAME);
    Kryo kryo         = pool.borrow();
    GameDatabase db   = null;
    if (dbFile.exists()) {
      db = kryo.readObject(new Input(dbFile.read()), GameDatabase.class);
    } else {
      db = new GameDatabase();
      db.bootstrap();
      saveDB(db);
    }
    pool.release(kryo);
    return db;
  }

  public void saveDB(GameDatabase db) {
    Kryo kryo         = pool.borrow();
    FileHandle dbFile = Gdx.files.internal(GameDatabase.FILE_NAME);
    try {
      Output output = new Output(new FileOutputStream(dbFile.file(), false));
      kryo.writeObject(output, db);
      output.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    pool.release(kryo);
  }


}
