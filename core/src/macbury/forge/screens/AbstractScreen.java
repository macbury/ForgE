package macbury.forge.screens;

import com.badlogic.gdx.Screen;

/**
 * Created by macbury on 15.10.14.
 */
public abstract class AbstractScreen implements Screen {
  private boolean initialized = false;

  public void initializeOnce() {
    if (!initialized)
      initialize();
    initialized = true;
  }

  protected abstract void initialize();
}
