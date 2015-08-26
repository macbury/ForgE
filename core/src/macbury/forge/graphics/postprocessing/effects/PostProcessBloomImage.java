package macbury.forge.graphics.postprocessing.effects;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.postprocessing.PostProcessingEffect;
import macbury.forge.level.env.LevelEnv;

/**
 * Created by macbury on 26.08.15.
 */
public class PostProcessBloomImage extends PostProcessingEffect {
  private static final String SHADER_BLOOM = "ps-bloom";

  @Override
  public void run(RenderContext context, LevelEnv levelEnv) {
    ForgE.fb.copyFB(context, levelEnv, Fbo.FRAMEBUFFER_MAIN_COLOR, Fbo.FRAMEBUFFER_DOWN_SAMPLED_MAIN);
    ForgE.fb.renderFB(context, levelEnv, Fbo.FRAMEBUFFER_BLOOM, SHADER_BLOOM);
  }

  @Override
  public void dispose() {

  }
}
