package macbury.forge.editor.parell.jobs.changeable;

import macbury.forge.editor.parell.Job;
import macbury.forge.editor.undo_redo.Changeable;

/**
 * Created by macbury on 14.08.15.
 */
public abstract class BaseChangeableJob extends Job<Changeable> {
  protected Changeable changeable;

  public BaseChangeableJob(Changeable changeable) {
    super(Changeable.class);
    this.changeable = changeable;
  }

  @Override
  public boolean isBlockingUI() {
    return true;
  }

  @Override
  public boolean performCallbackOnOpenGlThread() {
    return true;
  }

  @Override
  public void dispose() {
    changeable = null;
  }
}
