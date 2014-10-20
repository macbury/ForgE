package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

/**
 * Created by macbury on 19.10.14.
 */
public class Renderable extends Component implements Pool.Poolable {
  public BaseRenderable instance;
  public boolean visible = true;

  public Renderable() {
  }

  @Override
  public void reset() {
    instance = null;
    visible  = true;
  }
}
