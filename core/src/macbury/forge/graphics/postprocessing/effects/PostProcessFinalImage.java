package macbury.forge.graphics.postprocessing.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.postprocessing.PostProcessingEffect;

/**
 * Created by macbury on 26.08.15.
 */
public class PostProcessFinalImage extends PostProcessingEffect {
  @Override
  public void run() {
    ForgE.fb.begin(Fbo.FRAMEBUFFER_FINAL); {
      ForgE.graphics.clearAll(Color.BLACK);
    } ForgE.fb.end();
  }

  @Override
  public void dispose() {

  }
}
