package macbury.forge.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.storage.StorageManager;
import macbury.forge.storage.serializers.FullLevelStateSerializer;
import macbury.forge.storage.serializers.LevelStateBasicInfoSerializer;

import java.io.*;
import java.util.HashMap;

/**
 * Created by macbury on 09.03.15.
 */
public class LevelManager {
  private static final String TAG = "LevelManager";
  private final StorageManager storageManager;
  private HashMap<Integer, FileHandle> idToPathMap;

  private FileFilter mapAndDirFileFilter = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      return pathname.getName().endsWith(LevelState.FILE_EXT) || pathname.isDirectory();
    }
  };

  public LevelManager(StorageManager storageManager) {
    this.storageManager = storageManager;
    this.idToPathMap    = new HashMap<Integer, FileHandle>();
    reload();

  }

  public void save(LevelState state) {
    Kryo kryo          = storageManager.pool.borrow();
    FileHandle mapFile = Gdx.files.internal(LevelState.MAP_STORAGE_DIR+LevelState.MAP_NAME_PREFIX+state.getId()+LevelState.FILE_EXT);
    Gdx.app.log(TAG, "Saving map: " + mapFile.toString());
    try {
      Output output = new Output(new FileOutputStream(mapFile.file(), false));
      kryo.writeObject(output, state, new FullLevelStateSerializer());
      output.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    storageManager.pool.release(kryo);
  }

  private void getHandles(FileHandle begin, Array<FileHandle> handles)  {
    FileHandle[] newHandles = begin.list(mapAndDirFileFilter);
    for (FileHandle f : newHandles) {
      if (f.isDirectory()) {
        getHandles(f, handles);
      } else {
        handles.add(f);
      }
    }
  }

  public void reload() {
    Kryo kryo                                = storageManager.pool.borrow();
    Array<FileHandle> tempFiles              = new Array<FileHandle>();
    LevelStateBasicInfoSerializer serializer = new LevelStateBasicInfoSerializer();
    getHandles(Gdx.files.internal(LevelState.MAP_STORAGE_DIR) ,tempFiles);
    for (FileHandle file : tempFiles) {
      if (!file.isDirectory()) {
        Gdx.app.log(TAG, file.toString());
        try {
          Input input = new Input(new FileInputStream(file.file()));
          LevelState levelState = kryo.readObject(input, LevelState.class, serializer);
          Gdx.app.log(TAG, levelState.getName());
          input.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }

    }
    storageManager.pool.release(kryo);
  }

}
