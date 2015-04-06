package macbury.forge.utils;

import com.badlogic.gdx.math.MathUtils;
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
  public static final Vector3i ZERO = new Vector3i(0, 0, 0);
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

  public Vector3i sub(int ox, int oy, int oz) {
    x -= ox;
    y -= oy;
    z -= oz;
    return this;
  }

  public float dst(final Vector3i vector) {
    final float a = vector.x - x;
    final float b = vector.y - y;
    final float c = vector.z - z;
    return (float)Math.sqrt(a * a + b * b + c * c);
  }

  public float dst2 (Vector3i point) {
    final float a = point.x - x;
    final float b = point.y - y;
    final float c = point.z - z;
    return a * a + b * b + c * c;
  }

  public Vector3i abs() {
    this.x = Math.abs(x);
    this.y = Math.abs(y);
    this.z = Math.abs(z);
    return this;
  }

  public boolean isOneDirection() {
    byte dirs = 0;
    if (x != 0)
      dirs++;
    if (y != 0)
      dirs++;
    if (z != 0)
      dirs++;
    return dirs == 1;
  }

  public boolean isZero() {
    return x == 0 && y == 0 && z == 0;
  }

  public Vector3i add(float nx, float ny, float nz) {
    add((int)nx, (int)ny, (int)nz);
    return this;
  }

  public Vector3i nor() {
    this.abs();
    this.x = x >= 1 ? 1 : 0;
    this.y = y >= 1 ? 1 : 0;
    this.z = z >= 1 ? 1 : 0;
    return this;
  }

  public Vector3i scl(Vector3i other) {
    this.x *= other.x;
    this.y *= other.y;
    this.z *= other.z;
    return this;
  }

  public Vector3 cpyTo(Vector3 tempC) {
    return tempC.set(x, y,z);
  }
}
