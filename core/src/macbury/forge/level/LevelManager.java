package macbury.forge.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.storage.StorageManager;
import macbury.forge.storage.serializers.level.FullLevelStateSerializer;
import macbury.forge.storage.serializers.level.LevelStateBasicInfoSerializer;

import java.io.*;
import java.util.HashMap;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by macbury on 09.03.15.
 */
public class LevelManager {
  private static final String TAG = "LevelManager";
  private final StorageManager storageManager;
  private final LevelStateBasicInfoSerializer basicLevelInfoSerializer;
  private HashMap<Integer, FileHandle> idToPathMap;

  public FileFilter mapAndDirFileFilter = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      return pathname.getName().endsWith(LevelState.FILE_EXT) || pathname.isDirectory();
    }
  };

  public LevelManager(StorageManager storageManager) {
    this.storageManager = storageManager;
    this.idToPathMap    = new HashMap<Integer, FileHandle>();
    this.basicLevelInfoSerializer = new LevelStateBasicInfoSerializer();
    reload();
  }

  public LevelState load(FileHandle mapFile) {
    Kryo kryo             = storageManager.pool.borrow();
    LevelState levelState = null;
    Gdx.app.log(TAG, "Loading map: " + mapFile.toString());
    try {
      InflaterInputStream inflaterInputStream = new InflaterInputStream(new FileInputStream(mapFile.file()));
      Input input                             = new Input(inflaterInputStream);
      levelState                              = kryo.readObject(input, LevelState.class, new FullLevelStateSerializer());
      input.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    storageManager.pool.release(kryo);
    return levelState;
  }

  public void save(LevelState state, String storeDir) {
    Kryo kryo          = storageManager.pool.borrow();
    File file          = new File(storeDir + File.separator + LevelState.MAP_NAME_PREFIX+state.getId()+LevelState.FILE_EXT);
    if (file.exists()) {
      file.delete();
    }
    Gdx.app.log(TAG, "Saving map: " + file.getAbsolutePath());
    try {
      synchronized (state) {
        DeflaterOutputStream outputStream = new DeflaterOutputStream(new FileOutputStream(file, false));
        Output output                     = new Output(outputStream);
        kryo.writeObject(output, state, new FullLevelStateSerializer());
        output.close();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    storageManager.pool.release(kryo);
  }

  public void save(LevelState state) {
    String storeDir = Gdx.files.internal(LevelState.MAP_STORAGE_DIR).file().getAbsolutePath();
    if (exists(state.id)) {
      storeDir = getFileHandle(state.id).file().getParent();
    }
    save(state, storeDir);
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
    getHandles(Gdx.files.internal(LevelState.MAP_STORAGE_DIR) ,tempFiles);
    for (FileHandle file : tempFiles) {
      if (!file.isDirectory()) {
        int levelId = getLevelId(file);
        idToPathMap.put(levelId, file);
      }
    }
    storageManager.pool.release(kryo);
  }

  public LevelState loadBasicLevelStateInfo(int levelId) {
    LevelState levelState = null;
    Kryo kryo             = storageManager.pool.borrow();
    FileHandle mapFile    = getLevelFileHandle(levelId);
    Gdx.app.log(TAG, "Loading map: " + mapFile.toString());
    try {
      InflaterInputStream inflaterInput       = new InflaterInputStream(new FileInputStream(mapFile.file()));
      Input input                             = new Input(inflaterInput);
      levelState                              = kryo.readObject(input, LevelState.class, basicLevelInfoSerializer);
      input.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    storageManager.pool.release(kryo);
    return levelState;
  }

  private FileHandle getLevelFileHandle(int levelId) {
    return idToPathMap.get(levelId);
  }

  public int getLevelId(FileHandle file) {
    return Integer.valueOf(file.nameWithoutExtension().replaceAll(LevelState.MAP_NAME_PREFIX, ""));
  }

  public FileHandle getFileHandle(int id) {
    return idToPathMap.get(id);
  }

  public LevelState load(int levelId) {
    FileHandle handle = getFileHandle(levelId);
    if (handle == null) {
      throw new GdxRuntimeException("Map not found with id: " + levelId);
    }
    return load(handle);
  }

  public boolean exists(int levelId) {
    FileHandle handle = getFileHandle(levelId);
    return handle != null && handle.exists();
  }
}
