package macbury.forge.graphics.batch.sprites;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.batch.VoxelBatch;

/**
 * Created by macbury on 06.08.15.
 */
public class TextureSprite3D extends BaseSprite3D {
  protected Sprite3DCache spriteCache;
  protected TextureRegion textureRegion;
  private TextureAsset asset;
  private Texture texture;

  public TextureSprite3D(VoxelBatch manager) {
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
        textureRegion = new TextureRegion(getTexture());
        //textureRegion.setTexture();
        //textureRegion.setRegion(0,0,1,1);
      }
      spriteCache = manager.findSpriteCacheForRegion(textureRegion);
    }
    return spriteCache;
  }

  @Override
  public Texture getTexture() {
    if (texture == null) {
      texture = asset.get();
    }
    return texture;
  }


  @Override
  public void dispose() {
    texture = null;
    textureRegion = null;
    if (asset != null) {
      asset.release();
      asset = null;
    }
  }
}
