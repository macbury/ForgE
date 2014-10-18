package macbury.forge.shaders.utils;

/**
 * Created by macbury on 16.10.14.
 */
public interface ShaderReloadListener {
  public void onShadersReload(ShadersManager shaderManager);
  public void onShaderError(ShadersManager shaderManager, BaseShader program);
}
