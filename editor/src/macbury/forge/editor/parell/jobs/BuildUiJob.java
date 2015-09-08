package macbury.forge.editor.parell.jobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import macbury.forge.ForgE;
import macbury.forge.editor.parell.Job;
import macbury.forge.ui.UIManager;

/**
 * Created by macbury on 04.09.15.
 */
public class BuildUiJob extends Job<Boolean> {

  public static final String RAW_UI_IMAGES_PATH = "raw/ui/images";

  public BuildUiJob() {
    super(Boolean.class);
  }

  @Override
  public boolean isBlockingUI() {
    return true;
  }

  @Override
  public boolean performCallbackOnOpenGlThread() {
    return true;
  }

  @Override
  public Boolean perform() {

    TexturePacker.Settings settings = new TexturePacker.Settings();
    settings.grid = true;
    settings.square = true;
    settings.paddingX = 2;
    settings.paddingY = 2;
    TexturePacker.process(settings, Gdx.files.internal(RAW_UI_IMAGES_PATH).path(), Gdx.files.internal(UIManager.STORE_PATH).path(), "ui.atlas");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public void dispose() {

  }
}
