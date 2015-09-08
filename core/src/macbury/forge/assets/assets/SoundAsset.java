package macbury.forge.assets.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by macbury on 08.09.15.
 */
public class SoundAsset extends Asset<Sound> {
  @Override
  protected Sound loadObject(FileHandle file) {
    return Gdx.audio.newSound(file);
  }

  public void play() {
    get().play();
  }
}
