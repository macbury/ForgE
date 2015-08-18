package macbury.forge.graphics.light;

import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.camera.ICamera;
import macbury.forge.graphics.camera.OrtographicGameCamera;
import macbury.forge.graphics.fbo.Fbo;

/**
 * Created by macbury on 14.08.15.
 */
public class OrthographicDirectionalLight extends DirectionalLight implements Disposable {
  private final OrtographicGameCamera shadowCamera;
  private Vector3 _lightPosition;
  private Vector3 _sunPosition;
  private Vector3 _lightDirection;
  private Vector3 _originPosition;

  public OrthographicDirectionalLight() {
    super();
    this._lightPosition = new Vector3();
    this._sunPosition   = new Vector3();
    _lightDirection     = new Vector3();
    _originPosition     = new Vector3();
    this.shadowCamera   = new OrtographicGameCamera(80, 80);
    shadowCamera.near = -1f;

    this.shadowCamera.update(true);
  }

  public void update(GameCamera mainCamera) {
    shadowCamera.far  = mainCamera.far;
    _originPosition.set(mainCamera.direction).scl(shadowCamera.far).add(mainCamera.position);
    _lightPosition.set(_originPosition);//.scl(shadowCamera.far/ 2).add(_originPosition);
    //_sunPosition.set(direction).scl(mainCamera.far);

    // set position of ortho shadowCamera to match maincamera
    //_lightPosition.set(mainCamera.position);
    //_lightDirection.set(mainCamera.direction).scl(mainCamera.far / 2);
    //_lightPosition.add(_lightDirection);
    //_lightPosition.y = Math.min(_lightPosition.y, 0);
    //_lightPosition.add(_sunPosition);

    //tempVec.set(mainCamera.direction).scl(mainCamera.far/2).add(mainCamera.position).y += 2;
    this.shadowCamera.position.set(_lightPosition);
    //this.shadowCamera.direction.set(direction);
    this.shadowCamera.update();
  }

  public ICamera getShadowCamera() {
    return shadowCamera;
  }


  @Override
  public void dispose() {

  }
}
