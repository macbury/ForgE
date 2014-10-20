package macbury.forge.input;


import java.util.ArrayList;

/**
 * Created by macbury on 17.03.14.
 */
public class KeyMapping {

  private ArrayList<Integer> keycodes;
  private String name;
  private InputManager.Action action;

  public KeyMapping() {
    this.keycodes = new ArrayList<Integer>();
  }

  public InputManager.Action getAction() {
    return action;
  }

  public void setAction(InputManager.Action action) {
    this.action = action;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<Integer> getKeycodes() {
    return keycodes;
  }

  public void setKeycodes(ArrayList<Integer> keycodes) {
    this.keycodes = keycodes;
  }

  public boolean have(int keycode) {
    return this.keycodes.indexOf(keycode) != -1;
  }

  public KeyMapping addKeyCode(int keycode) {
    if (keycodes.indexOf(keycode) == -1) {
      this.keycodes.add(keycode);
    }

    return this;
  }
}
