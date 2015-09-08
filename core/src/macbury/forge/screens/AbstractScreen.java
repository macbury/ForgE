package macbury.forge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import macbury.forge.ForgE;

/**
 * Created by macbury on 15.10.14.
 */
public abstract class AbstractScreen implements Screen {
  private boolean initialized = false;

  public void initializeOnce() {
    if (!initialized)
      onInitialize();
    initialized = true;
  }

  protected abstract void onInitialize();
}
