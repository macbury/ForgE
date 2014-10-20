package macbury.forge.utils;

/**
 * Created by macbury on 19.10.14.
 */
public class Vector3Int {
  public int x;
  public int y;
  public int z;

  public Vector3Int(int nx, int ny, int nz){
    set(nx, ny, nz);
  }

  public Vector3Int() {
    this(0,0,0);
  }

  public Vector3Int set(int nx, int ny, int nz){
    this.x = nx;
    this.y = ny;
    this.z = nz;
    return this;
  }

  public Vector3Int add(int nx, int ny, int nz) {
    this.x += nx;
    this.y += ny;
    this.z += nz;
    return this;
  }

  public Vector3Int set(Vector3Int other) {
    set(other.x, other.y, other.z);
    return this;
  }

  public Vector3Int add(Vector3Int other) {
    add(other.x, other.y, other.z);
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (Vector3Int.class.isInstance(obj)) {
      Vector3Int other = (Vector3Int)obj;
      return (other.x == x && other.y == y && other.z == z);
    } else {
      return super.equals(obj);
    }
  }
}
