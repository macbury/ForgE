package macbury.forge.editor.systems;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by macbury on 26.10.14.
 */
public class MousePosition extends Vector2 {
  private boolean dirty = false;

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public Vector2 set(float x, float y) {
    setDirty(true);
    return super.set(x, y);
  }
}
