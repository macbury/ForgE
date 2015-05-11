package macbury.forge.editor.parell.jobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import macbury.forge.ForgE;
import macbury.forge.editor.parell.Job;

/**
 * Created by macbury on 11.11.14.
 */
public class BuildBlocksTexture extends Job<Boolean> {

  public BuildBlocksTexture() {
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
    TexturePacker.process(settings, Gdx.files.internal("raw-blocks").path(), Gdx.files.internal("graphics/textures/").path(), "tilemap.atlas");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    ForgE.blocks.reload();
    return true;
  }
}
