package macbury.forge.editor.undo_redo;

import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 31.10.14.
 */
public abstract class Changeable implements Disposable {
  public abstract void revert();
  public abstract void apply();
  public boolean runInThread() {
    return false;
  }
}
