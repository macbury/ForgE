package macbury.forge.editor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglInput;
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
      trigger();
      return true;
    } else {
      return false;
    }
  }

  public boolean isKeycode(int otherKeycode) {
    return this.keycode == otherKeycode;
  }

  public void trigger() {
    for (KeyShortcutListener listener : listeners) {
      listener.onKeyShortcut(this);
    }
  }

  public void addListener(KeyShortcutListener listener) {
    if (listeners.indexOf(listener, true) == -1) {
      listeners.add(listener);
    }
  }

  public void removeListener(KeyShortcutListener listener) {
    listeners.add(listener);
  }

  public String prettyName() {
    String s = "";
    LwjglInput.getLwjglKeyCode(modifier);
    return s;
  }


  public interface KeyShortcutListener {
    public void onKeyShortcut(KeyShortcutMapping shortcutMapping);
  }
}
