package macbury.forge.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.screens.LoadingScreen;

public class DesktopLauncher {
  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.resizable      = false;
    //config.width          = 1920;
    //config.height         = 1080;
    config.width          = 480 * 2;
    config.height         = 320 * 2;
    config.foregroundFPS  = 30;
    config.fullscreen     = false;

    Config forgeConfig        = new Config();
    forgeConfig.cacheGeometry = true; //TODO: check in terrain engine if there is geometry in file

    ForgE engine              = new ForgE(forgeConfig);
    engine.addBootListener(new ForgEBootListener() {
      @Override
      public void afterEngineCreate(ForgE engine) {
        ForgE.screens.set(new LoadingScreen(1055));
      }
    });
    new LwjglApplication(engine, config);
  }
}
