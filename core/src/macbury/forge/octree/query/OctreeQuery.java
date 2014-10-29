package macbury.forge.octree.query;

import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;

/**
 * Created by macbury on 29.10.14.
 */
public abstract class OctreeQuery {
  public abstract boolean checkNode(OctreeNode node);
  public abstract boolean checkObject(OctreeObject object);
}
