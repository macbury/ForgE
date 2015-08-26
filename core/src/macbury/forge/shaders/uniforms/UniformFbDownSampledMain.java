package macbury.forge.shaders.uniforms;

import macbury.forge.graphics.fbo.Fbo;

/**
 * Created by macbury on 26.08.15.
 */
public class UniformFbDownSampledMain extends UniformFrameBuffer {
  private static final String UNIFORM_NAME = "u_downSampledMainTexture";

  @Override
  public String getFrameBufferName() {
    return Fbo.FRAMEBUFFER_DOWN_SAMPLED_MAIN;
  }

  @Override
  public String getUniformName() {
    return UNIFORM_NAME;
  }
}
