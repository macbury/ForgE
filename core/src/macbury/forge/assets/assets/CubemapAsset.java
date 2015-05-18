package macbury.forge.assets.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by macbury on 18.05.15.
 */
public class CubemapAsset extends AssetWithDependencies<Cubemap> {
  private static final String SIDES[] = { "_xpos", "_xneg", "_ypos", "_yneg", "_zpos", "_zneg" };

  @Override
  protected Cubemap loadObject(FileHandle file) {
    Array<TextureData> textures = new Array<TextureData>();
    for(String sideName : SIDES) {
      FileHandle sideHandle = Gdx.files.internal(file.pathWithoutExtension() + sideName + "." + file.extension());
      if (sideHandle.exists()) {
        TextureAsset sideAsset = manager.getTexture(sideHandle.file().getAbsolutePath());
        addDependency(sideAsset);
        textures.add(sideAsset.get().getTextureData());
      } else {
        throw new GdxRuntimeException("Could not find " + sideHandle.path());
      }
    }
    return new Cubemap(
        textures.get(0),
        textures.get(1),
        textures.get(2),
        textures.get(3),
        textures.get(4),
        textures.get(5)
    );
  }
}
