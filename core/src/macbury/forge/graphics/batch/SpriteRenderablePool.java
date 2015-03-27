package macbury.forge.graphics.batch;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.graphics.batch.renderable.SpriteRenderable;

/**
 * Created by macbury on 26.03.15.
 */
public class SpriteRenderablePool extends Pool<SpriteRenderable> {
  protected Array<SpriteRenderable> obtained = new Array<SpriteRenderable>();
  @Override
  protected SpriteRenderable newObject () {
    return new SpriteRenderable();
  }
  @Override
  public SpriteRenderable obtain () {
    SpriteRenderable renderable = super.obtain();
    renderable.mesh = null;
    obtained.add(renderable);
    return renderable;
  }
  public void flush () {
    super.freeAll(obtained);
    obtained.clear();
  }
}