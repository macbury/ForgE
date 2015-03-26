package macbury.forge.editor.selection;

/**
 * Created by macbury on 25.03.15.
 */
public class EventSelection extends SingleBlockSelection {

  @Override
  public boolean isAppendSelectType() {
    return true;
  }

  @Override
  public boolean isReplaceSelectType() {
    return false;
  }
}
