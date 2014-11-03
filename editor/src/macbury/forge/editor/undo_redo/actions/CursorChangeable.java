package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.undo_redo.Changeable;
import macbury.forge.utils.VoxelCursor;

/**
 * Created by macbury on 03.11.14.
 */
public abstract class CursorChangeable extends Changeable {
  protected VoxelCursor from;
  protected VoxelCursor to;
  protected BoundingBox applyBox;

  public CursorChangeable(AbstractSelection selection) {
    this.from = new VoxelCursor();
    this.to   = new VoxelCursor();
    this.from.set(selection.getStartPosition());
    this.to.set(selection.getEndPostion());
    applyBox = new BoundingBox();
    applyBox.set(selection.getBoundingBox());
  }
}
