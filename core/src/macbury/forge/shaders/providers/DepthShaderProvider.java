package macbury.forge.shaders.providers;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.shaders.utils.*;

/**
 * Created by macbury on 19.05.15.
 */
public class DepthShaderProvider implements ShaderReloadListener, ShaderProvider {
  private final Array<RenderableBaseShader> shaders = new Array<RenderableBaseShader>();

  public DepthShaderProvider() {
    reloadShaderCache();
    ForgE.shaders.addOnShaderReloadListener(this);
  }

  private void reloadShaderCache() {
    shaders.clear();
    for (BaseShader shader : ForgE.shaders.all()) {
      if (DepthRenderableBaseShader.class.isInstance(shader)) {
        shaders.add((RenderableBaseShader) shader);
      }
    }
  }

  @Override
  public RenderableBaseShader provide(Renderable renderable) {
    for (RenderableBaseShader shader : shaders) {
      if (shader.canRender(renderable)) {
        return shader;
      }
    }

    throw new GdxRuntimeException("No shader for: " + renderable.getClass().getSimpleName());
  }

  @Override
  public void dispose() {
    ForgE.shaders.removeOnShaderReloadListener(this);
  }

  @Override
  public void onShadersReload(ShadersManager shaderManager) {
    reloadShaderCache();
  }

  @Override
  public void onShaderError(ShadersManager shaderManager, BaseShader program) {

  }
}
