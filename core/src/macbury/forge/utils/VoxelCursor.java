package macbury.forge.utils;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 01.11.14.
 */
public class VoxelCursor {
  public final Vector3i replace = new Vector3i();
  public final Vector3i append  = new Vector3i();

  public VoxelCursor set(VoxelCursor otherCursor) {
    replace.set(otherCursor.replace);
    append.set(otherCursor.append);
    return this;
  }

  public VoxelCursor add(Vector3 toAdd) {
    replace.add(toAdd);
    append.add(toAdd);
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (VoxelCursor.class.isInstance(obj)) {
      VoxelCursor otherCursor = (VoxelCursor)obj;
      return otherCursor.replace.equals(this.replace) && otherCursor.append.equals(append);
    } else {
      return super.equals(obj);
    }
  }
}
