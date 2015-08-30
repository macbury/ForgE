package macbury.forge.graphics.postprocessing.steps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.FrameBufferShader;
import macbury.forge.shaders.utils.BaseShader;

/**
 * Created by macbury on 28.08.15.
 */
public class ShaderPostProcessingStep extends PostProcessingStep {
  private String targetFboName;
  private FrameBufferShader shader;

  public ShaderPostProcessingStep(String target, FrameBufferShader frameBufferShader, PostProcessingManager manager) {
    this.shader = frameBufferShader;
    this.targetFboName = target;
  }

  @Override
  public void run(RenderContext renderContext, LevelEnv env) {
    renderContext.begin(); {
      ForgE.fb.begin(targetFboName); {
        ForgE.graphics.clearAll(Color.CLEAR);// TODO change it using json
        shader.begin(ForgE.fb.getScreenCamera(), renderContext, env); {
          shader.render(ForgE.fb.getScreenQuad(), GL30.GL_TRIANGLE_STRIP);
        } shader.end();
      } ForgE.fb. end();
    } renderContext.end();
  }

  @Override
  public void dispose() {
    this.shader.dispose();
    shader = null;
  }
}
