package macbury.forge.octree;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 03.08.15.
 */
public class OctreeMultiplexer implements Disposable {
  private final Array<OctreeNode> octrees;

  public OctreeMultiplexer() {
    octrees = new Array<OctreeNode>();
  }

  @Override
  public void dispose() {
    octrees.clear();
  }
}
