package macbury.forge.shaders.uniforms;

import macbury.forge.graphics.fbo.Fbo;

/**
 * Created by macbury on 26.08.15.
 */
public class UniformFbBloomTexture extends UniformFrameBuffer {
  private static final String SHADER_NAME = "u_bloomTexture";

  @Override
  public String getFrameBufferName() {
    return Fbo.FRAMEBUFFER_BLOOM;
  }

  @Override
  public String getUniformName() {
    return SHADER_NAME;
  }
}
