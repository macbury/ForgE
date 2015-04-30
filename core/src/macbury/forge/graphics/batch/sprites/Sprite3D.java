package macbury.forge.graphics.batch.sprites;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.mesh.Material;

/**
 * Created by macbury on 27.05.14.
 */
public class Sprite3D extends BaseSprite3D {
  protected Sprite3DCache spriteCache;
  protected TextureRegion textureRegion;
  private TextureAsset asset;

  public Sprite3D(VoxelBatch manager) {
    super(manager);
  }

  public void setTextureAsset(TextureAsset asset) {
    this.asset         = asset;
  }

  @Override
  public Mesh getMesh() {
    return getSpriteCache() != null ? getSpriteCache().mesh : null;
  }

  private Sprite3DCache getSpriteCache() {
    if (spriteCache == null) {
      if (textureRegion == null) {
        textureRegion = new TextureRegion(asset.get());
        //textureRegion.setTexture();
        //textureRegion.setRegion(0,0,1,1);
      }
      spriteCache = manager.findSpriteCacheForRegion(textureRegion);
    }
    return spriteCache;
  }

  @Override
  public Texture getTexture() {
    if (asset.isLoaded()) {
      return asset.get();
    }
    return null;
  }

}