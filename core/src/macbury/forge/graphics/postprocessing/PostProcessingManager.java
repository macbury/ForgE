package macbury.forge.graphics.postprocessing;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.postprocessing.effects.PostProcessFinalImage;

/**
 * Created by macbury on 26.08.15.
 */
public class PostProcessingManager implements Disposable {
  private Array<PostProcessingEffect> effects;

  public PostProcessingManager() {
    this.effects = new Array<PostProcessingEffect>();
    this.effects.add(new PostProcessFinalImage());
  }

  public void render() {
    for (int i = 0; i < effects.size; i++) {
      effects.get(i).run();
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
