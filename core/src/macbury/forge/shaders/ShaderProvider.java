package macbury.forge.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.SpriteRenderable;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.RenderableBaseShader;
import macbury.forge.shaders.utils.ShaderReloadListener;
import macbury.forge.shaders.utils.ShadersManager;

/**
 * Created by macbury on 29.10.14.
 */
public class ShaderProvider implements Disposable, ShaderReloadListener {
  private final Array<RenderableBaseShader> shaders = new Array<RenderableBaseShader>();

  public ShaderProvider() {
    reloadShaderCache();
    ForgE.shaders.addOnShaderReloadListener(this);
  }

  private void reloadShaderCache() {
    shaders.clear();
    for (BaseShader shader : ForgE.shaders.all()) {
      if (RenderableBaseShader.class.isInstance(shader)) {
        shaders.add((RenderableBaseShader) shader);
      }
    }
  }

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
