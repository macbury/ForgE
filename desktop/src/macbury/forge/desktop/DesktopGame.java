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
import macbury.forge.scripts.ScriptThread;
import macbury.forge.utils.ArgsParser;

import javax.swing.*;

/**
 * Created by macbury on 24.03.15.
 */
public class DesktopGame implements ForgEBootListener, Thread.UncaughtExceptionHandler, ScriptThread.Listener {
  public DesktopGame(String[] args) {
    SwingThemeHelper.useGTK();

    Thread.setDefaultUncaughtExceptionHandler(this);
    Config forgeConfig                   = Config.load("game");
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.resizable      = false;
    config.foregroundFPS  = 30;

    config.width          = forgeConfig.getInt(Config.Key.ResolutionWidth);
    config.height         = forgeConfig.getInt(Config.Key.ResolutionHeight);
    config.fullscreen     = forgeConfig.getBool(Config.Key.Fullscreen);

    ForgE engine              = new ForgE(forgeConfig, args);
    engine.addBootListener(this);
    new LwjglApplication(engine, config);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    ForgE.blocks.loadAtlasAndUvsIfNull();
    ForgE.scripts.loadAndRun(this);
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

  @Override
  public void onRubyError(Throwable e) {
    TaskDialogs.showException(e);
    e.printStackTrace();
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        Gdx.app.exit();
      }
    });
  }
}
