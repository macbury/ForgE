package macbury.forge.editor.selection;

import com.badlogic.gdx.Input;

/**
 * Created by macbury on 06.11.14.
 */
public class EreaseSelection extends BoxSelection {
  @Override
  public SelectType getSelectType() {
    return SelectType.Replace;
  }

  @Override
  public boolean shouldProcessMouseButton(int mouseButton) {
    return (Input.Buttons.LEFT == mouseButton );
  }
}
