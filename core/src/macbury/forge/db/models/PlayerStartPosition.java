package macbury.forge.db.models;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 24.03.15.
 */
public class PlayerStartPosition {
  public Vector3i voxelPosition;
  public int mapId;

  public Teleport toTeleport() {
    return new Teleport(voxelPosition, mapId);
  }
}
