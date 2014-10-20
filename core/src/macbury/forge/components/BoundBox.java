package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.level.map.ChunkMap;

/**
 * Created by macbury on 20.10.14.
 */
public class BoundBox extends Component implements Pool.Poolable {
  public BoundingBox box = new BoundingBox();
  @Override
  public void reset() {
    box.set(Vector3.Zero, ChunkMap.TILE_SIZE);
  }
}
