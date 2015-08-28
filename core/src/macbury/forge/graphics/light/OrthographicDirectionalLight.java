package macbury.forge.graphics.light;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.Config;
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
  private final float halfHeight;
  private final float halfDepth;
  private OrtographicGameCamera shadowCamera;
  public Matrix4 nearMatrix;
  public Matrix4 farMatrix;
  private BoundingSphereDirectionalAnalyzer frustrumAnalyzer;
  private float oldNear;
  private float oldFar;
  protected final Vector3 tmpV = new Vector3();
  public OrthographicDirectionalLight() {
    super();

    float shadowViewportWidth = 30;
    float shadowViewportHeight = 30;
    float shadowNear    = 0.1f;
    float shadowFar     = 100;

    this.shadowCamera   = new OrtographicGameCamera(shadowViewportWidth, shadowViewportHeight);
    shadowCamera.near   = shadowNear;
    shadowCamera.far    = shadowFar;

    halfHeight          = shadowViewportHeight * 0.5f;
    halfDepth           = shadowNear + 0.5f * (shadowFar - shadowNear);

    frustrumAnalyzer    = new BoundingSphereDirectionalAnalyzer();
    this.shadowCamera.update(true);
    this.nearMatrix     = new Matrix4();
    this.farMatrix      = new Matrix4();
  }
/*
  private void update(GameCamera mainCamera) {
    frustrumAnalyzer.analyze(mainCamera.normalOrDebugFrustrum(), direction).set(shadowCamera);
  }*/

  public void update (final Camera camera) {
    update(tmpV.set(camera.direction).scl(halfHeight), camera.direction);
  }

  public void update (final Vector3 center, final Vector3 forward) {
    // cam.position.set(10,10,10);
    shadowCamera.position.set(direction).scl(-halfDepth).add(center);
    shadowCamera.direction.set(direction).nor();
    // cam.up.set(forward).nor();
    shadowCamera.normalizeUp();
    shadowCamera.update();
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
    mainCamera.near = ForgE.config.getInt(Config.Key.NearShadowDistance);
    mainCamera.update(true);
    update(mainCamera);
  }


  public void endFar(GameCamera mainCamera) {
    farMatrix.set(shadowCamera.combined);
    restoreNearFar(mainCamera);
  }

  public void end(GameCamera mainCamera) {
    farMatrix.set(shadowCamera.combined);
    restoreNearFar(mainCamera);
  }

  public void beginNear(GameCamera mainCamera) {
    cacheNearFar(mainCamera);
    mainCamera.far = ForgE.config.getInt(Config.Key.NearShadowDistance);
    mainCamera.update(true);
    update(mainCamera);
    nearMatrix.set(shadowCamera.combined);
  }

  public void endNear(GameCamera mainCamera) {
    nearMatrix.set(shadowCamera.combined);
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
