package macbury.forge.shaders.attributes;

import com.badlogic.gdx.graphics.g3d.Attribute;

/**
 * Created by macbury on 20.07.15.
 */
public class SolidTerrainAttribute extends Attribute {
  public final static String Alias = "solid-terrain";
  public final static long Type = register(Alias);
  public SolidTerrainAttribute() {
    super(Type);
  }

  @Override
  public Attribute copy() {
    return new SolidTerrainAttribute();
  }
}
