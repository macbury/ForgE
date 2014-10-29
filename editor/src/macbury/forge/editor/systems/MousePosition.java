package macbury.forge.editor.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.camera.GameCamera;

/**
 * Created by macbury on 26.10.14.
 */
public class MousePosition extends Vector2 {
  private final GameCamera camera;
  private boolean dirty = false;
  public Vector3 vec3 = new Vector3();

  public MousePosition(GameCamera camera) {
    this.camera = camera;
    setZero();
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public Vector2 set(float x, float y) {
    setDirty(true);
    y = camera.viewportHeight - y;
    vec3.set(x,y,0);
    return super.set(x, y);
  }
}
