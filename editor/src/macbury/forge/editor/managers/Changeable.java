package macbury.forge.editor.managers;

/**
 * Created by macbury on 31.10.14.
 */
public abstract class Changeable {
  public abstract void undo();
  public abstract void redo();
}
