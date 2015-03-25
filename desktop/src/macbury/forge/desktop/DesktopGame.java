package macbury.forge.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ezware.dialog.task.TaskDialogs;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.screens.LoadingScreen;

import javax.swing.*;

/**
 * Created by macbury on 24.03.15.
 */
public class DesktopGame implements ForgEBootListener, Thread.UncaughtExceptionHandler {

  public DesktopGame() {
    Thread.setDefaultUncaughtExceptionHandler(this);
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.resizable      = false;
    config.width          = 1360;
    config.height         = 768;
    //config.width          = 480 * 2;
    //config.height         = 320 * 2;
    config.foregroundFPS  = 30;
    config.fullscreen     = false;

    Config forgeConfig        = new Config();
    forgeConfig.cacheGeometry = true; //TODO: check in terrain engine if there is geometry in file

    ForgE engine              = new ForgE(forgeConfig);
    engine.addBootListener(this);
    new LwjglApplication(engine, config);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    if (ForgE.db.startPosition == null) {
      throw new GdxRuntimeException("Start position not found!");
    } else {
      ForgE.screens.set(new LoadingScreen(ForgE.db.startPosition.toTeleport()));
    }
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    TaskDialogs.showException(e);
    e.printStackTrace();
    Gdx.app.exit();
  }
}
