package macbury.forge.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.ForgE;
import macbury.forge.db.models.BaseModel;
import macbury.forge.db.models.Teleport;
import macbury.forge.utils.Vector3i;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by macbury on 07.11.14.
 */
public class GameDatabase {
  public static final String FILE_NAME = "db/game.config";
  private static final String TAG = "GameDatabase";
  public String title;
  public int currentyUID = 0;
  public long build      = 0;
  public int lastOpenedMapId = -1;
  public Teleport startPosition;

  /**
   * Create int uuid
   * @return unique id
   */
  public int uid() {
    return currentyUID += 1;
  }

  /**
   * Bootstrap db with default values
   */
  public void bootstrap() {
    title = "ForgE Project";
    currentyUID = 0;
  }

  public void setStartPosition(int levelId, Vector3i voxelPosition) {
    Gdx.app.log(TAG, "New player start position: " + levelId + " at " + voxelPosition.toString());
    this.startPosition = new Teleport(voxelPosition, levelId);
    save();
  }

  public void save(BaseModel model) {
    Kryo kryo = ForgE.storage.begin(); {
      FileHandle modelFile = model.getFileHandle();
      try {
        Output output = new Output(new FileOutputStream(modelFile.file(), false));
        kryo.writeObject(output, model);
        output.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } ForgE.storage.end(kryo);
  }

  public void save() {
    ForgE.storage.saveDB(this);
  }
}
