package macbury.forge.editor.selection;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.utils.VoxelCursor;

/**
 * Created by macbury on 01.11.14.
 */
public class SingleBlockSelection extends AbstractSelection {

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
      startPosition.set(voxelCursor);
      endPostion.set(voxelCursor);
    }
  }

  @Override
  public void end(VoxelCursor voxelCursor) {
    startPosition.set(voxelCursor);
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
      startPosition.append.applyTo(out);
    } else {
      startPosition.replace.applyTo(out);
    }
  }

  @Override
  protected void getMinimum(Vector3 out) {
    if(isAppendSelectType()) {
      endPostion.append.applyTo(out);
    } else {
      endPostion.replace.applyTo(out);
    }

    out.add(voxelSize);
  }

}
