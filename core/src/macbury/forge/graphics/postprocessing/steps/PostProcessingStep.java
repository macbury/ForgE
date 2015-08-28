package macbury.forge.graphics.postprocessing.steps;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import macbury.forge.level.env.LevelEnv;

/**
 * Created by macbury on 28.08.15.
 */
public abstract class PostProcessingStep implements Disposable {


  public abstract void run(RenderContext renderContext, LevelEnv env);
}
