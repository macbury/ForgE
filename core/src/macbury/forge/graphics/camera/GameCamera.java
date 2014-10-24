package macbury.forge.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.frustrum.DebugFrustrum;

/**
 * Created by macbury on 23.10.14.
 */
public class GameCamera extends PerspectiveCamera {
  private static final float BASE_FOV = 67;
  private final Vector3 debugDirection;
  private DebugFrustrum debugFrustrum;

  public GameCamera() {
    super(BASE_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    debugDirection = new Vector3();
  }

  public void saveDebugFrustrum() {
    this.update();
    this.debugFrustrum = new DebugFrustrum(frustum, invProjectionView);
    this.debugDirection.set(direction);
  }

  public boolean haveDebugFrustrum() {
    return debugFrustrum != null;
  }

  public DebugFrustrum getDebugFrustrum() {
    return debugFrustrum;
  }

  public void clearDebugFrustrum() {
    this.debugFrustrum = null;
  }

  /**
   * Return debug frustum if have or return normal frustrum
   * @return
   */
  public Frustum normalOrDebugFrustrum() {
    return haveDebugFrustrum() ? debugFrustrum : frustum;
  }

  public Vector3 normalOrDebugDirection() {
    return haveDebugFrustrum() ? debugDirection : direction;
  }
}
