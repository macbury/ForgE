package macbury.forge.graphics.camera;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import macbury.forge.graphics.frustrum.DebugFrustrum;

/**
 * Created by macbury on 14.08.15.
 */
public interface ICamera {
  void saveDebugFrustrum();
  void extendFov();
  void restoreFov();
  boolean haveDebugFrustrum();
  DebugFrustrum getDebugFrustrum();
  void clearDebugFrustrum();
  Frustum normalOrDebugFrustrum();
  boolean boundsInFrustum(BoundingBox testBounds);
}
