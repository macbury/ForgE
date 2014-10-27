package macbury.forge.editor.selection;

/**
 * Created by macbury on 26.10.14.
 */
public interface SelectionInterface {
  public void onSelectionStart(AbstractSelection selection);
  public void onSelectionChange(AbstractSelection selection);
  public void onSelectionEnd(AbstractSelection selection);
}
