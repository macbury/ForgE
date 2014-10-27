package macbury.forge.utils;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 19.10.14.
 */
public class Vector3Int {
  public static final Vector3Int TOP = new Vector3Int(0,1,0);
  public static final Vector3Int BOTTOM = new Vector3Int(0,-1,0);
  public static final Vector3Int BACK = new Vector3Int(0, 0, -1);
  public static final Vector3Int FRONT = new Vector3Int(0, 0, 1);
  public static final Vector3Int RIGHT = new Vector3Int(1, 0, 0);
  public static final Vector3Int LEFT = new Vector3Int(-1,0,0);
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

  public void setZero() {
    this.set(0,0,0);
  }

  public void set(Vector3 other) {
    this.set((int)other.x, (int)other.y, (int)other.z);
  }

  @Override
  public String toString() {
    return "[" + x + ":" + y + ":" + z + "]";
  }

  public boolean lessThan(Vector3Int other) {
    return x < other.x && y < other.y && other.z < other.z;
  }
}
