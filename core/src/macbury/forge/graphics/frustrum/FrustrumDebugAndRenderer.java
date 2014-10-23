package macbury.forge.graphics.frustrum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.input.InputManager;

public class FrustrumDebugAndRenderer  extends InputAdapter implements Disposable {
  private final static short[][] RENDER_SEQUENCE = {
    {0,1,1,2,2,3,3,0},
    {4,5,5,6,6,7,7,4},
    {0,4,1,5,2,6,3,7}
  };
  private static final String TAG = "FrustrumRenderer";
  private final GameCamera frustrumCamera;
  private ShapeRenderer renderer;

  private boolean enabled;

  public FrustrumDebugAndRenderer(GameCamera camera) {
    this.frustrumCamera = camera;
    this.renderer       = new ShapeRenderer();
    ForgE.input.addProcessor(this);
  }


  public void render(PerspectiveCamera camera) {
    if (!isEnabled()) {
      return;
    }
    renderer.setProjectionMatrix(camera.combined);
    renderer.begin(ShapeRenderer.ShapeType.Line);
    renderer.setColor(Color.WHITE);


    for(int sequence = 0; sequence < RENDER_SEQUENCE.length; sequence++) {
      for(int index = 1; index < RENDER_SEQUENCE[sequence].length; index+=2) {
        short a = RENDER_SEQUENCE[sequence][index];
        short b = RENDER_SEQUENCE[sequence][index-1];

        renderer.line(frustrumCamera.getDebugFrustrum().planePoints[a], frustrumCamera.getDebugFrustrum().planePoints[b]);
      }
    }

    renderer.end();
  }

  @Override
  public boolean keyDown(int keycode) {
    if ( ForgE.input.isEqual(InputManager.Action.TestFrustrum, keycode)) {
      this.enabled = !this.enabled;
      if (enabled) {
        frustrumCamera.saveDebugFrustrum();
        Gdx.app.log(TAG, "Enabled frustrum debugger");
      } else {
        frustrumCamera.clearDebugFrustrum();
        Gdx.app.log(TAG, "Disabled frustrum debugger");
      }
      return true;
    } else {
      return false;
    }
  }

  public DebugFrustrum getFrustrum() {
    return frustrumCamera.getDebugFrustrum();
  }

  public boolean isEnabled() {
    return frustrumCamera.haveDebugFrustrum();
  }

  @Override
  public void dispose() {
    ForgE.input.removeProcessor(this);
  }
}
