package macbury.forge.octree.query;

import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 03.08.15.
 */
public abstract class OctreeEach<T> extends OctreeQuery {
  @Override
  public boolean checkNode(OctreeNode node) {
    return false;
  }

  @Override
  public boolean checkObject(OctreeObject object) {
    return false;
  }

  public abstract void found(T object);
}
