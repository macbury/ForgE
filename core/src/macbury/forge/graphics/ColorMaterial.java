package macbury.forge.graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by macbury on 20.10.14.
 */
public class ColorMaterial extends Color {

  public ColorMaterial(float r, float g, float b, float a) {
    super(r, g, b, a);
  }

  public static ColorMaterial air() {
    return new ColorMaterial(0,0,0,0);
  }

  public boolean isAir() {
    return a == 0.0f;
  }
}
