package macbury.forge.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ezware.dialog.task.TaskDialogs;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.screens.LoadingScreen;
import macbury.forge.screens.test.TestModelsScreen;
import macbury.forge.utils.ArgsParser;

import javax.swing.*;

/**
 * Created by macbury on 24.03.15.
 */
public class DesktopGame implements ForgEBootListener, Thread.UncaughtExceptionHandler {
  public DesktopGame(String[] arg) {
    SwingThemeHelper.useGTK();

    Thread.setDefaultUncaughtExceptionHandler(this);
    Config forgeConfig                   = Config.load("game");
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.resizable      = false;
    config.foregroundFPS  = 30;

    config.width          = forgeConfig.resolutionWidth;
    config.height         = forgeConfig.resolutionHeight;
    config.fullscreen     = forgeConfig.fullscreen;

    ForgE engine              = new ForgE(forgeConfig);
    engine.addBootListener(this);
    new LwjglApplication(engine, config);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    ForgE.blocks.loadAtlasAndUvsIfNull();
    ForgE.scripts.loadAndRun();
    /*if (ForgE.db.startPosition == null) {
      throw new GdxRuntimeException("Start position not found!");
    } else {
      ForgE.screens.set(new LoadingScreen(ForgE.db.startPosition));
      //ForgE.screens.set(new TestModelsScreen());
    }*/
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    TaskDialogs.showException(e);
    e.printStackTrace();
    Gdx.app.exit();
  }
}
