package macbury.forge.assets.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g3d.utils.TextureBinder;
import macbury.forge.assets.AssetsManager;

/**
 * Created by macbury on 08.03.15.
 */
public class TextureAsset extends Asset<Texture> {

  private Pixmap pixmap;

  @Override
  protected Texture loadObject(FileHandle file) {
    Texture texture = new Texture(file);
    texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    return texture;
  }

  public Pixmap getPixmap() {
    if (pixmap == null) {
      TextureData textureData = get().getTextureData();
      if (!textureData.isPrepared()) {
        textureData.prepare();
      }
      pixmap = textureData.consumePixmap();
    }
    return pixmap;
  }

  @Override
  public void dispose() {
    super.dispose();
    if (pixmap != null) {
      pixmap.dispose();
      pixmap = null;
    }
  }
}
