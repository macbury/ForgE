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
    config.width          = 1360;
    config.height         = 768;
    config.foregroundFPS  = 30;
    ForgE engine          = new ForgE(new Config());
    engine.addBootListener(new ForgEBootListener() {
      @Override
      public void afterEngineCreate(ForgE engine) {
        ForgE.screens.set(new LoadingScreen(1048));
      }
    });
    new LwjglApplication(engine, config);
  }
}
