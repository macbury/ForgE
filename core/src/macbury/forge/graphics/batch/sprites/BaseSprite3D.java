package macbury.forge.graphics.batch.sprites;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.graphics.batch.VoxelBatch;

/**
 * Created by macbury on 27.05.14.
 */
public abstract class BaseSprite3D {
  protected boolean staticSprite;
  protected VoxelBatch manager;

  protected Vector3 position;
  protected Quaternion rotation = new Quaternion();
  protected Vector2 scale       = new Vector2(1, 1);
  protected Matrix4 transform   = new Matrix4();
  protected boolean dirty       = true;
  protected final static Vector3 dir = new Vector3();
  protected static Vector3 tmp       = new Vector3();
  protected static Vector3 tmp2      = new Vector3();

  protected static final Vector3 X_AXIS = new Vector3(1, 0, 0);
  protected static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
  protected static final Vector3 Z_AXIS = new Vector3(0, 0, 1);
  private boolean transparent;

  public BaseSprite3D(VoxelBatch manager) {
    this.manager           = manager;
    this.position          = new Vector3();
  }

  public void init(boolean transparent, boolean staticSprite) {
    setTransparent(transparent);
    this.staticSprite      = staticSprite;
  }

  public void applyToMatrix(Matrix4 matrix) {
    if (dirty) {
      transform.idt();
      transform.translate(position);
      transform.scl(scale.x, scale.y, 1);
      transform.rotate(rotation);
      dirty = false;
    }

    matrix.set(transform);
  }

  public void set(float x, float y, float z) {
    position.set(x,y,z);
    dirty = true;
  }

  public void setTransparent(boolean transparent) {
    if (this.staticSprite) {
      throw new GdxRuntimeException("Cannot change transparency for static sprite!");
    }
    this.transparent = transparent;
  }

  public void scale(float x, float y) {
    scale.set(x,y);
    dirty = true;
  }

  public void lookAt(Vector3 position, Vector3 up) {
    dir.set(position).sub(this.position).nor();
    setRotation(dir, up);
  }

  public void setRotation(Vector3 dir, Vector3 up) {
    tmp.set(up).crs(dir).nor();
    tmp2.set(dir).crs(tmp).nor();
    rotation.setFromAxes(tmp.x, tmp2.x, dir.x, tmp.y, tmp2.y, dir.y, tmp.z, tmp2.z, dir.z);
    dirty = true;
  }

  public void setRotationX(float angle) {
    rotation.set(X_AXIS, angle);
    dirty = true;
  }

  public void setRotationY(float angle) {
    rotation.set(Y_AXIS, angle);
    dirty = true;
  }

  public void setRotationZ(float angle) {
    rotation.set(Z_AXIS, angle);
    dirty = true;
  }

  public abstract Mesh getMesh();

  public void set(Vector3 newPos) {
    this.position.set(newPos);
    dirty = true;
  }

  public boolean haveTransparency() {
    return transparent;
  }
}