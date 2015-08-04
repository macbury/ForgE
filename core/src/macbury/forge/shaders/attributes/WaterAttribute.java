package macbury.forge.shaders.attributes;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 20.07.15.
 */
public class WaterAttribute extends Attribute {
  public final static String Alias = "water";
  public final static long Type = register(Alias);
  public WaterAttribute() {
    super(Type);
  }

  @Override
  public Attribute copy() {
    return new WaterAttribute();
  }
}
