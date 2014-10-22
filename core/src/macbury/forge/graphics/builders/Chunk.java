package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 19.10.14.
 */
public class Chunk {
  public Vector3Int position  = new Vector3Int();
  public Vector3    worldPosition = new Vector3();
  public Vector3    size      = new Vector3();
  public Vector3Int start     = new Vector3Int();
  public Vector3Int end       = new Vector3Int();

  public boolean needRebuild  = true;

}
