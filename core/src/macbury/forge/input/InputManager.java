package macbury.forge.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;

import java.util.HashMap;

/**
 * Created by macbury on 20.10.14.
 */
public class InputManager extends InputMultiplexer {
  public enum Action {
    TestFrustrum,
    CameraLeft,
    CameraRight,
    CameraForward,
    CameraBackward,
    CameraRotateLeft,
    CameraRotateRight,
    CameraTiltForward,
    CameraMouseRotate, CameraTiltBackward
  }

  private HashMap<Action, KeyMapping> actions;

  public InputManager() {
    super();
    this.actions = new HashMap<Action, KeyMapping>();

    this.map(Action.TestFrustrum, Input.Keys.F, "Show current frustrum culling");

    this.map(Action.CameraLeft, Input.Keys.A, "Move camera left").addKeyCode(Input.Keys.LEFT);
    this.map(Action.CameraRight, Input.Keys.D, "Move camera right").addKeyCode(Input.Keys.RIGHT);

    this.map(Action.CameraForward, Input.Keys.W, "Move camera forward").addKeyCode(Input.Keys.UP);
    this.map(Action.CameraBackward, Input.Keys.S, "Move camera backward").addKeyCode(Input.Keys.DOWN);

    this.map(Action.CameraRotateLeft, Input.Keys.Q, "Rotate camera left");
    this.map(Action.CameraRotateRight, Input.Keys.E, "Rotate camera right");
    this.map(Action.CameraMouseRotate, Input.Keys.CONTROL_LEFT, "Rotate camera using mouse");

    this.map(Action.CameraTiltForward, Input.Keys.Z, "Rotate camera forward");
    this.map(Action.CameraTiltBackward, Input.Keys.X, "Rotate camera backward");
  }

  public KeyMapping map(Action name, int keyCode, String description) {
    if (!this.actions.containsKey(name)) {
      KeyMapping key = new KeyMapping();
      key.setAction(name);
      key.addKeyCode(keyCode);
      key.setName(description);
      this.actions.put(name, key);
    }
    return this.actions.get(name);
  }


  public boolean isEqual(Action action, int keyCode) {
    return this.actions.get(action).have(keyCode);
  }


}
