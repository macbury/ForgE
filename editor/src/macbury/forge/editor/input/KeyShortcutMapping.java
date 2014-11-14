package macbury.forge.editor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 14.11.14.
 */
public class KeyShortcutMapping {
  private final int modifier;
  private final int keycode;
  private final Array<KeyShortcutListener> listeners;

  public KeyShortcutMapping(int modifier, int keycode) {
    this.modifier  = modifier;
    this.keycode   = keycode;
    this.listeners = new Array<KeyShortcutListener>();
  }

  public boolean canHandle(int keycode) {
    if ((this.keycode == keycode && Gdx.input.isKeyPressed(modifier))) {
      for (KeyShortcutListener listener : listeners) {
        listener.onKeyShortcut(this);
      }
      return true;
    } else {
      return false;
    }
  }

  public void addListener(KeyShortcutListener listener) {
    listeners.add(listener);
  }

  public void removeListener(KeyShortcutListener listener) {
    listeners.add(listener);
  }

  public interface KeyShortcutListener {
    public void onKeyShortcut(KeyShortcutMapping shortcutMapping);
  }
}
