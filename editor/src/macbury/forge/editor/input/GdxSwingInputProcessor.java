package macbury.forge.editor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by macbury on 13.11.14.
 */
public class GdxSwingInputProcessor extends InputAdapter implements KeyListener {
  private boolean catchedShortcut = false;
  @Override
  public boolean keyDown(int keycode) {
    Gdx.app.log("gdx", "key pressed: " + keycode);
    if (keycode == Input.Keys.Z && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
      Gdx.app.log("gdx", "undo pressed: " + keycode);
      catchedShortcut = true;
      return true;
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

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    Gdx.app.log("swing", "key: "+e.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  public class GdxSwingMapping {

  }
}
