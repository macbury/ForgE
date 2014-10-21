package macbury.forge.octree;

/**
 * Created by macbury on 21.10.14.
 */
public enum OctreePart {
  FrontTopLeft(0), FrontTopRight(1), FrontBottomLeft(2), FrontBottomRight(3),
  BackTopLeft(4), BackTopRight(5), BackBottomLeft(6), BackBottomRight(7);

  private final int index;
  OctreePart(int ni) {
    this.index = ni;
  }

  public int getIndex() {
    return index;
  }
}
