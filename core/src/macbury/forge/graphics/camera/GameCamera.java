package macbury.forge.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Frustum;
import macbury.forge.graphics.frustrum.DebugFrustrum;

/**
 * Created by macbury on 23.10.14.
 */
public class GameCamera extends PerspectiveCamera {
  private static final float BASE_FOV = 67;
  private DebugFrustrum debugFrustrum;

  public GameCamera() {
    super(BASE_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  public void saveDebugFrustrum() {
    this.update();
    this.debugFrustrum = new DebugFrustrum(frustum, invProjectionView);
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
}
