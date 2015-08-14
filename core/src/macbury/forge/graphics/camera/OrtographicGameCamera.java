package macbury.forge.graphics.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.graphics.frustrum.DebugFrustrum;

/**
 * Created by macbury on 14.08.15.
 */
public class OrtographicGameCamera extends OrthographicCamera implements ICamera {

  private DebugFrustrum debugFrustrum;

  public OrtographicGameCamera(float viewportWidth, float viewportHeight) {
    super(viewportWidth, viewportHeight);
  }

  @Override
  public void saveDebugFrustrum() {
    this.debugFrustrum = new DebugFrustrum(frustum, invProjectionView);
  }

  @Override
  public void extendFov() {

  }

  @Override
  public void restoreFov() {

  }

  @Override
  public boolean haveDebugFrustrum() {
    return debugFrustrum != null;
  }

  @Override
  public DebugFrustrum getDebugFrustrum() {
    return debugFrustrum;
  }

  @Override
  public void clearDebugFrustrum() {
    debugFrustrum = null;
  }

  @Override
  public Frustum normalOrDebugFrustrum() {
    return debugFrustrum == null ? frustum : debugFrustrum;
  }

  @Override
  public boolean boundsInFrustum(BoundingBox testBounds) {
    return normalOrDebugFrustrum().boundsInFrustum(testBounds);
  }
}
