package macbury.forge.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.graphics.frustrum.DebugFrustrum;

/**
 * Created by macbury on 23.10.14.
 */
public class GameCamera extends PerspectiveCamera {
  protected static final float BASE_FOV = 67;
  protected static final float EXTEND_FOV_BY = 15;
  protected final Vector3 debugDirection;
  protected final Vector3 debugPosition;
  protected DebugFrustrum debugFrustrum;
  protected float oldFieldOfView;

  public GameCamera() {
    super(BASE_FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    debugDirection = new Vector3();
    this.debugPosition = new Vector3();
    this.far           = 100;
  }

  public GameCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
    super(fieldOfViewY, viewportWidth, viewportHeight);
    debugDirection = new Vector3();
    this.debugPosition = new Vector3();
    this.far           = 100;
  }

  public void saveDebugFrustrum() {
    this.debugFrustrum = new DebugFrustrum(frustum, invProjectionView);
    this.debugDirection.set(direction);
    debugPosition.set(position);
    restoreFov();
  }

  public void extendFov() {
    this.oldFieldOfView = this.fieldOfView;
    this.fieldOfView    += EXTEND_FOV_BY;
    this.update();
  }

  public void restoreFov() {
    this.fieldOfView = this.oldFieldOfView;
    this.update();
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
    if (haveDebugFrustrum()) {
      return debugFrustrum;
    } else {
      return frustum;
    }
  }

  public Vector3 normalOrDebugDirection() {
    return haveDebugFrustrum() ? debugDirection : direction;
  }

  public Vector3 normalOrDebugPosition() {
    return haveDebugFrustrum() ? debugPosition : position;
  }

  public boolean boundsInFrustum(BoundingBox testBounds) {
    return normalOrDebugFrustrum().boundsInFrustum(testBounds);
  }
}
