package macbury.forge.db;

import com.badlogic.gdx.Gdx;
import macbury.forge.ForgE;
import macbury.forge.db.models.PlayerStartPosition;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 07.11.14.
 */
public class GameDatabase {
  public static final String FILE_NAME = "game.db";
  private static final String TAG = "GameDatabase";
  public String title;
  public int currentyUID = 0;
  public long build      = 0;
  public PlayerStartPosition startPosition;
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
    startPosition               = new PlayerStartPosition();
    startPosition.mapId         = levelId;
    startPosition.voxelPosition = new Vector3i(voxelPosition);

    ForgE.storage.saveDB(this);
  }
}
