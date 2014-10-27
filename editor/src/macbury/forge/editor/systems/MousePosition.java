package macbury.forge.editor.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 26.10.14.
 */
public class MousePosition extends Vector2 {
  private boolean dirty = false;
  public Vector3 vec3 = new Vector3();
  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public Vector2 set(float x, float y) {
    setDirty(true);
    vec3.set(x,y,0);
    return super.set(x, y);
  }
}
