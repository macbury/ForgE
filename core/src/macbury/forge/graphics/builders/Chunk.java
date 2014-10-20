package macbury.forge.graphics.builders;

import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 19.10.14.
 */
public class Chunk {
  public Vector3Int position  = new Vector3Int();

  public Vector3Int start     = new Vector3Int();
  public Vector3Int end       = new Vector3Int();

  public boolean needRebuild  = true;

}
