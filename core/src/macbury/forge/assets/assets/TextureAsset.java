package macbury.forge.assets.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureBinder;
import macbury.forge.assets.AssetsManager;

/**
 * Created by macbury on 08.03.15.
 */
public class TextureAsset extends Asset<Texture> {

  @Override
  protected Texture loadObject(FileHandle file) {
    Texture texture = new Texture(file);
    texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

    return texture;
  }


}
