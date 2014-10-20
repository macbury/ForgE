package macbury.forge.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Created by macbury on 05.05.14.
 */
public class Overlay extends Widget {
  public Overlay() {
    super();
    setFillParent(true);
  }

  private void blur() {
    Stage s = getStage();
    if (s != null) {
      s.unfocus(this);
    }
  }


  public boolean focused() {
    return (getStage() != null) && (getStage().getKeyboardFocus() == this);
  }

  public void focus() {
    if (getStage() != null) {
      getStage().setKeyboardFocus(this);
      getStage().setScrollFocus(this);
    }
  }
}
