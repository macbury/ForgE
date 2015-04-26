package macbury.forge.shaders;

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
  private static final String TERRAIN_SHADER = "terrain";
  private static final String SPRITE_SHADER = "sprite";
  private RenderableBaseShader terrainShader;
  private RenderableBaseShader sprite3DShader;

  public ShaderProvider() {
    reloadShaderCache();
    ForgE.shaders.addOnShaderReloadListener(this);
  }

  private void reloadShaderCache() {
    this.terrainShader  = (RenderableBaseShader)ForgE.shaders.get(TERRAIN_SHADER);
    this.sprite3DShader = (RenderableBaseShader)ForgE.shaders.get(SPRITE_SHADER);
  }

  public RenderableBaseShader provide(BaseRenderable renderable) {
    if (SpriteRenderable.class.isInstance(renderable)) {
      return sprite3DShader;
    } else if (VoxelChunkRenderable.class.isInstance(renderable)) {
      return terrainShader;
    } else {
      throw new GdxRuntimeException("No shader for: " + renderable.getClass().getSimpleName());
    }
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
