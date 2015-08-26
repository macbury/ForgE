package macbury.forge.graphics.postprocessing.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import macbury.forge.ForgE;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.postprocessing.PostProcessingEffect;
import macbury.forge.level.env.LevelEnv;


/**
 * Created by macbury on 26.08.15.
 */
public class PostProcessFinalImage extends PostProcessingEffect {
  private static final String SHADER_FINAL = "ps-final";

  public PostProcessFinalImage() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void run(RenderContext context, LevelEnv levelEnv) {
    ForgE.fb.renderFB(context, levelEnv, Fbo.FRAMEBUFFER_FINAL, SHADER_FINAL);
  }
}
