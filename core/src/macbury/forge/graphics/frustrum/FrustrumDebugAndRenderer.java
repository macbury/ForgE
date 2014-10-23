package macbury.forge.graphics.frustrum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.input.InputManager;

public class FrustrumDebugAndRenderer  extends InputAdapter implements Disposable {
  private final static short[][] RENDER_SEQUENCE = {
    {0,1,1,2,2,3,3,0},
    {4,5,5,6,6,7,7,4},
    {0,4,1,5,2,6,3,7}
  };
  private static final String TAG = "FrustrumRenderer";
  private ShapeRenderer renderer;

  private PerspectiveCamera frustrumCamera;
  private DebugFrustrum frustrum;

  private boolean enabled;

  public FrustrumDebugAndRenderer(PerspectiveCamera camera) {
    this.frustrumCamera = camera;
    this.renderer       = new ShapeRenderer();
    saveState();
    ForgE.input.addProcessor(this);
  }

  public void saveState() {
    Gdx.app.log(TAG, "Saving state");
    this.frustrumCamera.update(true);
    this.frustrum = new DebugFrustrum(this.frustrumCamera.frustum, this.frustrumCamera.invProjectionView);
  }

  public void render(PerspectiveCamera camera) {
    if (!enabled) {
      return;
    }
    renderer.setProjectionMatrix(camera.combined);
    renderer.begin(ShapeRenderer.ShapeType.Line);
    renderer.setColor(Color.WHITE);


    for(int sequence = 0; sequence < RENDER_SEQUENCE.length; sequence++) {
      for(int index = 1; index < RENDER_SEQUENCE[sequence].length; index+=2) {
        short a = RENDER_SEQUENCE[sequence][index];
        short b = RENDER_SEQUENCE[sequence][index-1];
        renderer.line(frustrum.planePoints[a], frustrum.planePoints[b]);
      }
    }

    renderer.end();
  }

  @Override
  public boolean keyDown(int keycode) {
    if ( ForgE.input.isEqual(InputManager.Action.TestFrustrum, keycode)) {
      this.enabled = !this.enabled;
      if (enabled) {
        saveState();
        Gdx.app.log(TAG, "Enabled frustrum debugger");
      } else {
        Gdx.app.log(TAG, "Disabled frustrum debugger");
      }
      return true;
    } else {
      return false;
    }
  }

  public DebugFrustrum getFrustrum() {
    return frustrum;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void dispose() {
    ForgE.input.removeProcessor(this);
  }
}
