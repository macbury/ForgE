package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.graphics.camera.GameCamera;

/**
 * Created by macbury on 17.03.15.
 */
public class PlayerComponent extends BaseComponent {
  public GameCamera camera;
  public float headWobbleSpeed;
  public float headWobbleMax;

  public Vector3 cameraOffset = new Vector3();
  @Override
  public void reset() {
    this.camera = null;
    cameraOffset.setZero();
  }

  @Override
  public void set(BaseComponent otherComponent) {
    this.camera           = ((PlayerComponent)otherComponent).camera;
    this.headWobbleSpeed  = ((PlayerComponent) otherComponent).headWobbleSpeed;
    this.headWobbleMax    = ((PlayerComponent) otherComponent).headWobbleMax;
    this.cameraOffset.set(((PlayerComponent) otherComponent).cameraOffset);

  }
}
