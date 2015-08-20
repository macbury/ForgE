package macbury.forge.shaders.providers;

import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 20.08.15.
 */
public class DepthShaderProvider extends ShaderProvider {

  @Override
  public RenderableBaseShader provide(Renderable renderable) {
    RenderableBaseShader providedShader = super.provide(renderable);
    return providedShader.getDepthShader();
  }
}
