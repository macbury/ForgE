package macbury.forge.editor.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 13.11.14.
 */
public class GdxSwingInputProcessor extends InputAdapter {
  private final Array<KeyShortcutMapping> mappings;
  private boolean catchedShortcut = false;

  public GdxSwingInputProcessor() {
    this.mappings = new Array<KeyShortcutMapping>();
  }

  public KeyShortcutMapping registerMapping(int modifier, int keycode, KeyShortcutMapping.KeyShortcutListener listener) {
    KeyShortcutMapping mapping = new KeyShortcutMapping(modifier, keycode);
    this.mappings.add(mapping);
    mapping.addListener(listener);
    return mapping;
  }

  @Override
  public boolean keyDown(int keycode) {
    for (KeyShortcutMapping mapping : mappings) {
      if (mapping.canHandle(keycode)) {
        catchedShortcut = true;
        return true;
      }
    }
    return super.keyUp(keycode);
  }

  @Override
  public boolean keyTyped(char character) {
    return catchedShortcut;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (catchedShortcut) {
      catchedShortcut = false;
      return true;
    }
    return false;
  }

}
