package macbury.forge.graphics.batch;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 28.04.15.
 */
public class RenderablePool extends Pool<Renderable> {
  protected Array<Renderable> obtained = new Array<Renderable>();

  @Override
  protected Renderable newObject () {
    return new Renderable();
  }

  @Override
  public Renderable obtain () {
    Renderable renderable = super.obtain();
    renderable.environment = null;
    renderable.material = null;
    renderable.mesh = null;
    renderable.shader = null;
    obtained.add(renderable);
    return renderable;
  }

  public void flush () {
    super.freeAll(obtained);
    obtained.clear();
  }
}