package macbury.forge.editor.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 13.11.14.
 */
public class GdxSwingInputProcessor extends InputAdapter {
  private static final String TAG = "GdxSwingInputProcessor";
  private final Array<KeyShortcutMapping> mappings;
  private KeyShortcutMapping currentMapping;

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
    if (currentMapping != null) {
      return true;
    } else {
      //Gdx.app.log(TAG, "Triggered keycode: " + keycode);
      for (KeyShortcutMapping mapping : mappings) {
        if (mapping.canHandle(keycode)) {
          currentMapping = mapping;
          return true;
        }
      }
      return false;
    }
  }

  @Override
  public boolean keyTyped(char character) {
    return currentMapping != null;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (currentMapping != null) {
      currentMapping = null;
      return true;
    }
    return false;
  }

}
