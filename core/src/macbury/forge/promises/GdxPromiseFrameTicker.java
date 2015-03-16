package macbury.forge.promises;

import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 16.03.15.
 */
public class GdxPromiseFrameTicker implements Disposable {
  private GdxFutureTask target;

  public void setTarget(GdxFutureTask target) {
    this.target = target;
  }

  public GdxFutureTask getTarget() {
    return target;
  }

  public void update(float delta) {
    if (target != null) {
      target.tick(delta);
    }
  }

  @Override
  public void dispose() {
    target = null;
  }
}
