package macbury.forge.shaders.attributes;

import com.badlogic.gdx.graphics.g3d.Attribute;

/**
 * Created by macbury on 21.07.15.
 */
public class WaterElevationAttribute  extends Attribute {
  public final static String Alias = "water_elevation";
  public final static long Type = register(Alias);
  private final float height;
  public WaterElevationAttribute(float height) {
    super(Type);
    this.height = height;
  }

  public float getHeight() {
    return height;
  }

  @Override
  public Attribute copy() {
    return new WaterElevationAttribute(height);
  }

  @Override
  public int compareTo(Attribute o) {
    if (type != o.type) return type < o.type ? -1 : 1;
    return 0;
  }
}

