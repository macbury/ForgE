package macbury.forge.graphics.postprocessing;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.postprocessing.effects.PostProcessBloomImage;
import macbury.forge.graphics.postprocessing.effects.PostProcessFinalImage;
import macbury.forge.level.env.LevelEnv;


/**
 * Created by macbury on 26.08.15.
 */
public class PostProcessingManager implements Disposable {
  private Array<PostProcessingEffect> effects;

  public PostProcessingManager() {
    this.effects = new Array<PostProcessingEffect>();
    this.effects.add(new PostProcessBloomImage());
    this.effects.add(new PostProcessFinalImage());
  }

  public void render(RenderContext renderContext, LevelEnv env) {
    for (int i = 0; i < effects.size; i++) {
      effects.get(i).run(renderContext, env);
    }
  }

  @Override
  public void dispose() {
    for (PostProcessingEffect effect : effects) {
      effect.dispose();
    }
    effects.clear();
    effects = null;
  }
}
