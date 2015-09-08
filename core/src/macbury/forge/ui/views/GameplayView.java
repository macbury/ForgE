package macbury.forge.ui.views;

import macbury.forge.graphics.fbo.Fbo;

/**
 * Created by macbury on 02.09.15.
 */
public class GameplayView extends FullScreenFrameBufferResult {
  public GameplayView() {
    super(Fbo.FRAMEBUFFER_FINAL);
  }
}
