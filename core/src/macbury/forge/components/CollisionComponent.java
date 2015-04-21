package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by macbury on 21.03.15.
 */
public class CollisionComponent extends BaseComponent {
  public boolean solid          = false;
  public final Vector3 normal   = new Vector3(0,0,0);

  @Override
  public void reset() {
    solid = false;
    normal.set(1,1,1);
  }

  @Override
  public void set(BaseComponent otherComponent) {
    solid = ((CollisionComponent)otherComponent).solid;
  }
}
