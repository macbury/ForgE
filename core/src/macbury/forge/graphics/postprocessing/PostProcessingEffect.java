package macbury.forge.graphics.postprocessing;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.Level;
import macbury.forge.level.env.LevelEnv;

/**
 * Created by macbury on 26.08.15.
 */
public abstract class PostProcessingEffect implements Disposable {
  public abstract void run(RenderContext context, LevelEnv levelEnv);
}
