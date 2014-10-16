package macbury.forge.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by macbury on 16.10.14.
 */
public interface ShaderReloadListener {
  public void onShadersReload(ShadersManager shaderManager);
  public void onShaderError(ShadersManager shaderManager, ShaderProgram program);
}
