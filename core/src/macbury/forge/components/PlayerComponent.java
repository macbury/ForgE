package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 17.03.15.
 */
public class PlayerComponent extends Component implements Pool.Poolable {
  public Camera camera;
  @Override
  public void reset() {
    this.camera = null;
  }
}
