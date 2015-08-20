package macbury.forge.graphics.light;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.camera.ICamera;
import macbury.forge.graphics.camera.OrtographicGameCamera;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.shadows.BoundingSphereDirectionalAnalyzer;

/**
 * Created by macbury on 14.08.15.
 */
public class OrthographicDirectionalLight extends DirectionalLight implements Disposable {
  private OrtographicGameCamera shadowCamera;
  private BoundingSphereDirectionalAnalyzer frustrumAnalyzer;
  public OrthographicDirectionalLight() {
    super();

    this.shadowCamera   = new OrtographicGameCamera(5, 5);
    shadowCamera.near   = -1f;
    shadowCamera.far    = 50;
    frustrumAnalyzer    = new BoundingSphereDirectionalAnalyzer();
    this.shadowCamera.update(true);
  }

  public void update(GameCamera mainCamera) {
    frustrumAnalyzer.analyze(mainCamera.normalOrDebugFrustrum(), direction).set(shadowCamera);
  }

  public ICamera getShadowCamera() {
    return shadowCamera;
  }

  public OrthographicCamera getCamera() {
    return shadowCamera;
  }


  @Override
  public void dispose() {

  }
}
