package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 23.03.15.
 */
public abstract class BaseComponent extends Component implements Pool.Poolable {


  public abstract void set(BaseComponent otherComponent);
}
