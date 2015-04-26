package macbury.forge.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import macbury.forge.assets.assets.Asset;
import macbury.forge.db.GameDatabase;
import macbury.forge.db.models.PlayerStartPosition;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.level.LevelEnv;
import macbury.forge.level.LevelState;
import macbury.forge.storage.serializers.assets.AssetSerializer;
import macbury.forge.storage.serializers.db.GameDatabaseSerializer;
import macbury.forge.storage.serializers.db.models.PlayerStartPositionSerializer;
import macbury.forge.storage.serializers.graphics.*;
import macbury.forge.storage.serializers.graphics.math.*;
import macbury.forge.storage.serializers.level.ChunkMapDataSerializer;
import macbury.forge.storage.serializers.level.FullLevelStateSerializer;
import macbury.forge.storage.serializers.level.LevelEnvSerializer;
import macbury.forge.storage.serializers.level.VoxelSerializer;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

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
      kryo.register(LevelState.class, new FullLevelStateSerializer());
      kryo.register(ChunkMap.class, new ChunkMapDataSerializer());
      kryo.register(Voxel.class, new VoxelSerializer());
      kryo.register(Vector3.class, new Vector3Serializer());
      kryo.register(LevelEnv.class, new LevelEnvSerializer());
      kryo.register(Vector2.class, new Vector2Serializer());
      kryo.register(Color.class, new ColorSerializer());
      kryo.register(DirectionalLight.class, new DirectionalLightSerializer());
      kryo.register(Asset.class, new AssetSerializer());
      kryo.register(BoundingBox.class, new BoundingBoxSerializer());
      kryo.register(VoxelChunkRenderable.class, new VoxelFaceRenderableSerializer());
      kryo.register(Matrix4.class, new Matrix4Serializer());
      kryo.register(Vector3i.class, new Vector3iSerializer());
      kryo.register(PlayerStartPosition.class, new PlayerStartPositionSerializer());
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
