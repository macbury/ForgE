package macbury.forge.graphics.light;

import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.camera.ICamera;
import macbury.forge.graphics.camera.OrtographicGameCamera;

/**
 * Created by macbury on 14.08.15.
 */
public class OrthographicDirectionalLight extends DirectionalLight {
  private final OrtographicGameCamera shadowCamera;
  private Vector3 _lightPosition;
  private Vector3 _sunPosition;
  private Vector3 _lightDirection;
  public OrthographicDirectionalLight() {
    super();
    this._lightPosition = new Vector3();
    this._sunPosition   = new Vector3();
    _lightDirection     = new Vector3();
    this.shadowCamera = new OrtographicGameCamera(40, 40);
    shadowCamera.near = -1000.0f;
    shadowCamera.far  = 1000.0f;
    this.shadowCamera.update(true);
  }

  public void update(GameCamera mainCamera) {
    // set position of ortho shadowCamera to match maincamera
    _lightPosition.set(mainCamera.position).y += 0;
    //_lightDirection.set(mainCamera.direction).scl(mainCamera.far/2);
    _lightPosition.add(_lightDirection);
    _sunPosition.set(direction).scl(mainCamera.far);
    _lightPosition.add(_sunPosition);

    //tempVec.set(mainCamera.direction).scl(mainCamera.far/2).add(mainCamera.position).y += 2;
    this.shadowCamera.position.set(_lightPosition);
    this.shadowCamera.direction.set(direction);
    this.shadowCamera.update();
  }

  public ICamera getShadowCamera() {
    return shadowCamera;
  }
}
