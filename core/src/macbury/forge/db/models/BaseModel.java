package macbury.forge.db.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;

/**
 * Created by macbury on 11.05.15.
 */
public abstract class BaseModel {
  private static final String DB_PATH = "db/";

  public abstract String getFilename();
  public abstract String getStorageDir();

  public FileHandle getFileHandle() {
    return ForgE.files.internal(DB_PATH+ getStorageDir() + getFilename());
  }
}
