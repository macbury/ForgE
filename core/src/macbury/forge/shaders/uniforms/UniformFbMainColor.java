package macbury.forge.shaders.uniforms;

import macbury.forge.graphics.fbo.Fbo;

/**
 * Created by macbury on 26.08.15.
 */
public class UniformFbMainColor extends UniformFrameBuffer {
  private static final String UNIFORM_NAME = "u_mainColorTexture";

  @Override
  public String getFrameBufferName() {
    return Fbo.FRAMEBUFFER_MAIN_COLOR;
  }

  @Override
  public String getUniformName() {
    return UNIFORM_NAME;
  }
}
