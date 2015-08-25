package macbury.forge.graphics.light;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
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
  public Matrix4 nearMatrix;
  public Matrix4 farMatrix;
  private BoundingSphereDirectionalAnalyzer frustrumAnalyzer;
  private float oldNear;
  private float oldFar;

  public OrthographicDirectionalLight() {
    super();

    this.shadowCamera   = new OrtographicGameCamera(5, 5);
    shadowCamera.near   = 0.1f;
    shadowCamera.far    = 50;
    frustrumAnalyzer    = new BoundingSphereDirectionalAnalyzer();
    this.shadowCamera.update(true);
    this.nearMatrix     = new Matrix4();
    this.farMatrix      = new Matrix4();
  }

  private void update(GameCamera mainCamera) {
    frustrumAnalyzer.analyze(mainCamera.normalOrDebugFrustrum(), direction).set(shadowCamera);
  }

  private void cacheNearFar(GameCamera mainCamera) {
    this.oldNear = mainCamera.near;
    this.oldFar  = mainCamera.far;
  }

  private void restoreNearFar(GameCamera mainCamera) {
    mainCamera.near = oldNear;
    mainCamera.far  = oldFar;
  }

  public void begin(GameCamera mainCamera) {
    cacheNearFar(mainCamera);
    update(mainCamera);
    farMatrix.set(shadowCamera.combined);
  }

  public void beginFar(GameCamera mainCamera) {
    cacheNearFar(mainCamera);
    mainCamera.near = ForgE.config.nearShadowDistance;
    mainCamera.update(true);
    update(mainCamera);

    farMatrix.set(shadowCamera.combined);
  }

  public void beginNear(GameCamera mainCamera) {
    cacheNearFar(mainCamera);
    mainCamera.far = ForgE.config.nearShadowDistance;
    mainCamera.update(true);
    update(mainCamera);
    nearMatrix.set(shadowCamera.combined);
  }

  public void end(GameCamera mainCamera) {
    restoreNearFar(mainCamera);
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
