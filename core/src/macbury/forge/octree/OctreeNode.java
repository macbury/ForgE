package macbury.forge.octree;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 20.10.14.
 */
public class OctreeNode implements Pool.Poolable, Disposable {
  private int                 level;
  private Array<OctreeObject> objects;
  private Array<OctreeNode>   nodes;
  private BoundingBox         bounds;
  private final Vector3       tempA;
  private final Vector3       tempB;
  private OctreeNode          parent;

  private Vector3             center  = new Vector3();
  private Vector3             min     = new Vector3();
  private Vector3             max     = new Vector3();
  private BoundingBox         tempBox = new BoundingBox();
  private static final Pool<OctreeNode> octreeNodePool = new Pool<OctreeNode>() {
    @Override
    protected OctreeNode newObject() {
      return new OctreeNode();
    }
  };

  public OctreeNode() {
    this.tempA    = new Vector3();
    this.tempB    = new Vector3();
    this.level    = 0;
    this.objects  = new Array<OctreeObject>();
    this.nodes    = new Array<OctreeNode>();
    this.bounds   = new BoundingBox();
    this.parent   = null;
    clear();
  }


  public OctreeNode getNode(OctreePart part) {
    return nodes.get(part.getIndex());
  }

  public void split() {
    center = bounds.getCenter(center);
    bounds.getMin(min);
    bounds.getMax(max);

    buildNode(tempA.set(max).set(max.x, max.y, min.z), center, OctreePart.FrontTopLeft);
    buildNode(tempA.set(min).set(min.x, max.y, min.z), center, OctreePart.FrontTopRight);
    buildNode(min, center, OctreePart.FrontBottomRight);
    buildNode(tempA.set(max).set(max.x, min.y, min.z), center, OctreePart.FrontTopLeft);
    buildNode(tempA.set(min).set(min.x, max.y, max.z), center, OctreePart.BackTopRight);
    buildNode(max, center, OctreePart.BackTopLeft);
    buildNode(tempA.set(max).set(min.x, min.y, max.z), center, OctreePart.BackBottomRight);
    buildNode(tempA.set(max).set(max.x, min.y, max.z), center, OctreePart.BackBottomLeft);

  }

  private void buildNode(Vector3 min, Vector3 max, OctreePart part) {
    tempBox.set(min, max);
    OctreeNode nodeQuadrant = OctreeNode.node(level, tempBox);
    nodeQuadrant.setParent(this);
    nodes.add(nodeQuadrant);
  }

  public void getDimension(Vector3 out) {
    bounds.getDimensions(out);
  }

  public float getWidth() {
    bounds.getDimensions(tempA);
    return tempA.x;
  }

  public float getHeight() {
    bounds.getDimensions(tempA);
    return tempA.y;
  }

  public float getDepth() {
    bounds.getDimensions(tempA);
    return tempA.z;
  }

  public OctreeNode getParent() {
    return parent;
  }

  public void setParent(OctreeNode parent) {
    this.parent = parent;
  }

  public boolean haveNodes() {
    return nodes.size != 0;
  }

  public int getLevel() {
    return level;
  }

  public BoundingBox getBounds() {
    return bounds;
  }

  public void setBounds(BoundingBox box) {
    bounds.set(box);
    clear();
  }

  public void clear() {
    objects.clear();
    for (OctreeNode node : nodes) {
      octreeNodePool.free(node);
    }
    nodes.clear();
  }

  @Override
  public void reset() {
    this.level = 0;
    this.bounds.set(Vector3.Zero, Vector3.Zero);
    parent = null;
    clear();
  }

  public static OctreeNode root() {
    OctreeNode node = octreeNodePool.obtain();
    return node;
  }

  public static OctreeNode node(int parentLevel, BoundingBox box) {
    OctreeNode node = octreeNodePool.obtain();
    node.setLevel(parentLevel + 1);
    node.setBounds(box);
    return node;
  }

  @Override
  public void dispose() {
    clear();
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void bottomNodes(Array<OctreeNode> out) {
    if (haveNodes()) {
      for(OctreeNode node : nodes) {
        node.bottomNodes(out);
      }
    } else {
      out.add(this);
    }
  }
}
