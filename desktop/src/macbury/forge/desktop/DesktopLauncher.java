package macbury.forge.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.screens.TestMeshScreen;

public class DesktopLauncher {
  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.resizable = false;
    config.width     = 1360;
    config.height    = 768;
    ForgE engine     = new ForgE();
    engine.setBootListener(new ForgEBootListener() {
      @Override
      public void afterEngineCreate(ForgE engine) {
        engine.setScreen(new TestMeshScreen());
      }
    });
    new LwjglApplication(engine, config);
  }
}
