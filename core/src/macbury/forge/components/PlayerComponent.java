package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 17.03.15.
 */
public class PlayerComponent extends BaseComponent {
  public Camera camera;
  public float headWobbleSpeed;
  public float headWobbleMax;
  @Override
  public void reset() {
    this.camera = null;
  }

  @Override
  public void set(BaseComponent otherComponent) {
    this.camera           = ((PlayerComponent)otherComponent).camera;
    this.headWobbleSpeed  = ((PlayerComponent) otherComponent).headWobbleSpeed;
    this.headWobbleMax    = ((PlayerComponent) otherComponent).headWobbleMax;
  }
}
