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
    ForgE.blocks.reload();
    TexturePacker.process(Gdx.files.internal("textures/blocks").path(), Gdx.files.internal("textures/").path(), "tilemap.atlas");
    return true;
  }
}
