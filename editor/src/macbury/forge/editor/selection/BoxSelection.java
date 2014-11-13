package macbury.forge.editor.selection;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.utils.VoxelCursor;

/**
 * Created by macbury on 01.11.14.
 */
public class BoxSelection extends AbstractSelection {
  private final Vector3 tempA = new Vector3();
  private final Vector3 tempB = new Vector3();

  @Override
  public void start(VoxelCursor voxelCursor) {
    startPosition.set(voxelCursor);
    endPostion.set(voxelCursor);

    selecting = true;
  }

  @Override
  public void reset(VoxelCursor voxelCursor) {
    startPosition.set(voxelCursor);
    endPostion.set(voxelCursor);
  }

  @Override
  public void update(VoxelCursor voxelCursor) {
    if (selecting) {
      endPostion.set(voxelCursor);
    }
  }

  @Override
  public void end(VoxelCursor voxelCursor) {
    endPostion.set(voxelCursor);

    selecting = false;
  }

  @Override
  public boolean shouldProcessMouseButton(int mouseButton) {
    return (Input.Buttons.LEFT == mouseButton || Input.Buttons.RIGHT == mouseButton);
  }

  @Override
  protected void getMaximum(Vector3 out) {
    if(isAppendSelectType()) {
      startPosition.append.applyTo(tempA);
    } else {
      startPosition.replace.applyTo(tempA);
    }

    if(isAppendSelectType()) {
      endPostion.append.applyTo(tempB);
    } else {
      endPostion.replace.applyTo(tempB);
    }

    float ex = Math.max(this.tempB.x, this.tempA.x);
    float ey = Math.max(this.tempB.y, this.tempA.y);
    float ez = Math.max(this.tempB.z, this.tempA.z);

    out.set(ex,ey,ez).add(voxelSize);
  }

  @Override
  protected void getMinimum(Vector3 out) {
    if(isAppendSelectType()) {
      startPosition.append.applyTo(tempA);
    } else {
      startPosition.replace.applyTo(tempA);
    }

    if(isAppendSelectType()) {
      endPostion.append.applyTo(tempB);
    } else {
      endPostion.replace.applyTo(tempB);
    }

    float ex = Math.min(this.tempB.x, this.tempA.x);
    float ey = Math.min(this.tempB.y, this.tempA.y);
    float ez = Math.min(this.tempB.z, this.tempA.z);

    out.set(ex,ey,ez);
  }
}
