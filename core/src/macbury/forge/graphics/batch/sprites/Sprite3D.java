package macbury.forge.graphics.batch.sprites;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.mesh.Material;

/**
 * Created by macbury on 27.05.14.
 */
public class Sprite3D extends BaseSprite3D {
  protected Sprite3DCache spriteCache;
  protected TextureRegion textureRegion;
  public Sprite3D(VoxelBatch manager) {
    super(manager);
  }
  public void setTextureRegion(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
    if (staticSprite) {
      //this.material = manager.findOrInitializeMaterialForTextureRegion(textureRegion, blendingAttribute.blended);
    } else {
      //textureAttribute.textureDescription.texture = textureRegion.getTexture();
    }
    //spriteCache = manager.findOrInitializeSpriteCacheByMaterialAndTextureRegion(textureRegion);
  }

  @Override
  public Mesh getMesh() {
    return spriteCache.mesh;
  }

}