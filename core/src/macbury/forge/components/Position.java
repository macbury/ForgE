package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 19.10.14.
 */
public class Position extends Component implements Pool.Poolable {
  public final Vector3    vector;
  public final Quaternion rotation;
  public final Vector3    size;

  public Position() {
    this.vector   = new Vector3();
    this.rotation = new Quaternion();
    this.size     = new Vector3();
  }
  
  @Override
  public void reset() {
    vector.setZero();
    rotation.set(Vector3.Z, 90);
    size.set(1,1,1);
  }
}
