package macbury.forge.editor.parell.jobs.changeable;

import macbury.forge.editor.undo_redo.Changeable;

/**
 * Created by macbury on 14.08.15.
 */
public class RevertChangeableJob extends BaseChangeableJob {
  public RevertChangeableJob(Changeable changeable) {
    super(changeable);
  }

  @Override
  public Changeable perform() {
    changeable.revert();
    return changeable;
  }
}
