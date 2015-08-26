package macbury.forge.graphics.postprocessing;

import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 26.08.15.
 */
public abstract class PostProcessingEffect implements Disposable {
  public abstract void run();
}
