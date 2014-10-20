package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 20.10.14.
 */
public class Visible extends Component implements Pool.Poolable {
  public boolean visible = true;
  @Override
  public void reset() {

  }
}
