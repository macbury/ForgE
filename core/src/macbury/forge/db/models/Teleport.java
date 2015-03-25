package macbury.forge.db.models;

import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 24.03.15.
 */
public class Teleport {
  public final Vector3i voxelPosition;
  public final int mapId;

  public Teleport(Vector3i voxelPosition, int mapId) {
    this.voxelPosition = voxelPosition;
    this.mapId         = mapId;
  }
}
