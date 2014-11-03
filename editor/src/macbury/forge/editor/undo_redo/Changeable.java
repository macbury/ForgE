package macbury.forge.editor.undo_redo;

/**
 * Created by macbury on 31.10.14.
 */
public abstract class Changeable {
  public abstract void revert();
  public abstract void apply();
}
