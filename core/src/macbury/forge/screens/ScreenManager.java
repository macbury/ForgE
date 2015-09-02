package macbury.forge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import macbury.forge.ForgE;

/**
 * Created by macbury on 15.10.14.
 */
public class ScreenManager {
  private final ForgE engine;
  private AbstractScreen currentScreen;

  public ScreenManager(ForgE engine) {
    this.engine = engine;
  }

  public void set(AbstractScreen screen) {
    if (ForgE.isOpenGlCurrentThread()) {
      setCurrentScreen(screen);
    } else {
      Gdx.app.postRunnable(new Runnable() {
        @Override
        public void run() {
          setCurrentScreen(screen);
        }
      });
    }

  }

  private void setCurrentScreen(AbstractScreen screen) {
    currentScreen = screen;
    if (currentScreen != null) {
      currentScreen.initializeOnce();
    }
    engine.setScreen(screen);
  }

  public AbstractScreen current() {
    return currentScreen;
  }

  public void disposeCurrentScreen() {
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    engine.setScreen(null);
    currentScreen = null;
  }

  public void reset() {
    disposeCurrentScreen();
  }
}
