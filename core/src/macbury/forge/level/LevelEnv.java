package macbury.forge.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

/**
 * Created by macbury on 28.10.14.
 */
public class LevelEnv {
  public final DirectionalLight mainLight;
  public final Color ambientLight;
  public final Color skyColor;

  public LevelEnv() {
    skyColor     = Color.valueOf("3498db");
    mainLight    = new DirectionalLight();
    mainLight.set(1.0f, 1.0f, 1.0f,-1, -1, 0.5f);

    ambientLight = Color.valueOf("cccccc");
  }
}
