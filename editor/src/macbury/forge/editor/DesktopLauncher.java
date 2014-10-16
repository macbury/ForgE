package macbury.forge.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.shaders.ShaderReloadListener;
import macbury.forge.shaders.ShadersManager;

/**
 * Created by macbury on 15.10.14.
 */
public class DesktopLauncher {
  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    ForgE game                           = new ForgE();
    game.setBootListener(new ForgEBootListener() {
      @Override
      public void afterEngineCreate(ForgE engine) {
        ForgE.shaders.addOnShaderReloadListener(new ShaderReloadListener() {
          @Override
          public void onShadersReload(ShadersManager shaderManager) {

          }

          @Override
          public void onShaderError(ShadersManager shaderManager, ShaderProgram program) {

          }
        });
      }
    });
    new LwjglApplication(game, config);
  }
}
