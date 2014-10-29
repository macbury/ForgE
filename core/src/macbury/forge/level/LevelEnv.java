package macbury.forge.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

/**
 * Created by macbury on 28.10.14.
 */
public class LevelEnv {
  public final DirectionalLight mainLight;
  public final Color ambientLight;

  public LevelEnv() {
    mainLight    = new DirectionalLight();
    mainLight.set(1.4f, 1.4f, 1.4f, 0.2f, -0.3f, -0.54f);
    ambientLight = new Color( 0.4f, 0.4f, 0.4f, 1f);
  }
}
