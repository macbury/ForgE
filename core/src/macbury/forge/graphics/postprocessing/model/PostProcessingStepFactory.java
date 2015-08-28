package macbury.forge.graphics.postprocessing.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import macbury.forge.graphics.postprocessing.steps.CopyPostProcessingStep;
import macbury.forge.graphics.postprocessing.steps.PostProcessingStep;
import macbury.forge.graphics.postprocessing.steps.ShaderPostProcessingStep;

/**
 * Created by macbury on 28.08.15.
 */
public class PostProcessingStepFactory {
  public String copy;
  public String target;


  public String fragment;
  public String vertex;

  public ObjectMap<String, String> customUniforms;
  public Array<String> uniforms;

  public PostProcessingStep build() {
    setDefaults();

    if (copy != null) {
      return new CopyPostProcessingStep(copy, target);
    } else {
      return new ShaderPostProcessingStep();
    }
  }

  private void setDefaults() {
    if (vertex == null) {
      vertex = "default";
    }


  }
}
