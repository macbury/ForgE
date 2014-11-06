package macbury.forge.screens;

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
    currentScreen = screen;
    currentScreen.initializeOnce();
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
}
