package macbury.forge.graphics.postprocessing.steps;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.graphics.postprocessing.steps.PostProcessingStep;
import macbury.forge.level.env.LevelEnv;

/**
 * Created by macbury on 28.08.15.
 */
public class CopyPostProcessingStep extends PostProcessingStep {

  private String source;
  private String target;

  public CopyPostProcessingStep(String source, String target, PostProcessingManager manager) {
    this.source = source;
    this.target = target;

    if (source == null || target == null) {
      throw new GdxRuntimeException("Required source and target for copy!");
    }
  }

  @Override
  public void run(RenderContext renderContext, LevelEnv env) {
    ForgE.fb.copyFB(renderContext, env, source, target);
  }

  @Override
  public void dispose() {

  }
}
