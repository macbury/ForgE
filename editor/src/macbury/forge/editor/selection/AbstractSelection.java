package macbury.forge.editor.selection;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.utils.Vector3i;
import macbury.forge.utils.VoxelCursor;

/**
 * Created by macbury on 26.10.14.
 */
public abstract class AbstractSelection {
  protected VoxelCursor startPosition = new VoxelCursor();
  protected VoxelCursor endPostion    = new VoxelCursor();
  protected BoundingBox tempBox       = new BoundingBox();
  protected SelectType selectType     = SelectType.Append;

  private static final Vector3 tempA = new Vector3();
  private static final Vector3 tempB = new Vector3();

  public abstract void reset(VoxelCursor voxelCursor);
  public abstract void start(VoxelCursor voxelCursor);
  public abstract void update(VoxelCursor voxelCursor);
  public abstract void end(VoxelCursor voxelCursor);

  protected Vector3 voxelSize;
  protected boolean selecting         = false;

  public AbstractSelection(Vector3 voxelSize) {
    this.voxelSize = new Vector3(voxelSize);
  }

  public AbstractSelection() {
    this.voxelSize = new Vector3(1,1,1);
  }

  public SelectType getSelectType() {
    return selectType;
  }

  public void setSelectType(SelectType selectType) {
    this.selectType = selectType;
  }

  public boolean isAppendSelectType() {
    return getSelectType() == SelectType.Append;
  }

  public boolean isReplaceSelectType() {
    return getSelectType() == SelectType.Replace;
  }

  public BoundingBox getBoundingBox() {
    getMinimum(tempA);
    getMaximum(tempB);
    tempBox.set(tempB, tempA);
    return tempBox;
  }

  protected abstract void getMaximum(Vector3 out);
  protected abstract void getMinimum(Vector3 out);

  public Vector3i getStartPosition() {
    if (isAppendSelectType()) {
      return startPosition.append;
    } else {
      return startPosition.replace;
    }
  }

  public Vector3i getEndPostion() {
    if (isAppendSelectType()) {
      return endPostion.append;
    } else {
      return endPostion.replace;
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + startPosition.toString() + " - " + endPostion.toString();
  }

  public void setVoxelSize(Vector3 voxelSize) {
    this.voxelSize.set(voxelSize);
  }
}
