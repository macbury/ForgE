package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SelectType;
import macbury.forge.editor.undo_redo.Changeable;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 03.11.14.
 */
public abstract class CursorChangeable extends Changeable {
  protected Block.Side alignToSide;
  protected SelectType selectType;
  protected Vector3i from;
  protected Vector3i to;
  protected BoundingBox applyBox;

  protected Block blockPrimary;
  protected Block blockSecondary;
  private byte mouseButton;

  public CursorChangeable() {
    this.from = new Vector3i();
    this.to   = new Vector3i();
    applyBox  = new BoundingBox();

  }

  public void setSelection(AbstractSelection selection) {
    this.selectType  = selection.getSelectType();
    this.mouseButton = selection.getSelectedMouseButton();
    this.from.set(selection.getStartPosition());
    this.to.set(selection.getEndPostion());

    applyBox.set(selection.getBoundingBox());

    this.alignToSide = selection.getAlginSide();
  }

  public void setBlockPrimary(Block blockPrimary) {
    this.blockPrimary = blockPrimary;
  }

  public void setBlockSecondary(Block blockSecondary) {
    this.blockSecondary = blockSecondary;
  }

  public Block getBlockBySelectionMouse() {
    return mouseButton == Input.Buttons.LEFT ? blockPrimary : blockSecondary;
  }
}
