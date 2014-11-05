package macbury.forge.utils;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 19.10.14.
 */
public class Vector3i {
  public static final Vector3i TOP = new Vector3i(0,1,0);
  public static final Vector3i BOTTOM = new Vector3i(0,-1,0);
  public static final Vector3i BACK = new Vector3i(0, 0, -1);
  public static final Vector3i FRONT = new Vector3i(0, 0, 1);
  public static final Vector3i RIGHT = new Vector3i(1, 0, 0);
  public static final Vector3i LEFT = new Vector3i(-1,0,0);
  public int x;
  public int y;
  public int z;

  public Vector3i(int nx, int ny, int nz){
    set(nx, ny, nz);
  }

  public Vector3i() {
    this(0,0,0);
  }

  public Vector3i(Vector3i other) {
    this(other.x, other.y, other.z);
  }

  public Vector3i set(int nx, int ny, int nz){
    this.x = nx;
    this.y = ny;
    this.z = nz;
    return this;
  }

  public Vector3i add(int nx, int ny, int nz) {
    this.x += nx;
    this.y += ny;
    this.z += nz;
    return this;
  }

  public Vector3i set(Vector3i other) {
    set(other.x, other.y, other.z);
    return this;
  }

  public Vector3i add(Vector3i other) {
    add(other.x, other.y, other.z);
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (Vector3i.class.isInstance(obj)) {
      Vector3i other = (Vector3i)obj;
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
    return x + "x" + y + "x" + z;
  }

  public boolean lessThan(Vector3i other) {
    return x < other.x && y < other.y && other.z < other.z;
  }

  public void add(Vector3 other) {
    add((int)other.x, (int)other.y, (int)other.z);
  }

  public void sub(Vector3 other) {
    x -= other.x;
    y -= other.y;
    z -= other.z;
  }

  public void applyTo(Vector3 vectorToApply) {
    vectorToApply.set(x,y,z);
  }

  public Vector3i sub(Vector3i other) {
    x -= other.x;
    y -= other.y;
    z -= other.z;
    return this;
  }
}
